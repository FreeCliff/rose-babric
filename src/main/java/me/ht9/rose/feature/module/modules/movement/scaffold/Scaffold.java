package me.ht9.rose.feature.module.modules.movement.scaffold;

import me.ht9.rose.event.bus.annotation.SubscribeEvent;
import me.ht9.rose.event.events.PacketEvent;
import me.ht9.rose.event.events.PlayerMoveEvent;
import me.ht9.rose.event.events.PosRotUpdateEvent;
import me.ht9.rose.event.events.PosRotUpdateFinishEvent;
import me.ht9.rose.feature.module.Module;
import me.ht9.rose.feature.module.annotation.Description;
import me.ht9.rose.feature.module.setting.Setting;
import me.ht9.rose.mixin.accessors.ItemBlockAccessor;
import me.ht9.rose.util.world.Facing;
import net.minecraft.src.*;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Description(value = "Automatically place blocks under your feet.")
public final class Scaffold extends Module
{
    private static final Scaffold instance = new Scaffold();

    private final List<Block> INVALID_BLOCKS = Arrays.asList(Block.waterMoving, Block.waterStill, Block.fire, Block.lavaMoving, Block.lavaStill, Block.chest, Block.workbench, Block.gravel, Block.sand);

    private final Setting<Integer> size = new Setting<>("Size", 1, 1, 5);
    private final Setting<Tower> towerMode = new Setting<>("Tower", Tower.Motion);
    private final Setting<Swap> swap = new Setting<>("Swap", Swap.Server);
    private final Setting<Boolean> understacked = new Setting<>("Swap To Infinites", false, () -> !swap.value().equals(Swap.Manual));
    private final Setting<Boolean> swing = new Setting<>("Swing", true);
    private final Setting<Boolean> safeWalk = new Setting<>("SafeWalk", false);

    private int clientSlot = -1, slot = -1;
    private List<BlockPair> placements;

    @Override
    public void onDisable()
    {
        if (mc.thePlayer != null)
        {
            if (clientSlot != -1 && swap.value() == Swap.Client)
            {
                mc.thePlayer.inventory.currentItem = clientSlot;
            }

            mc.getSendQueue().addToSendQueue(
                    new Packet16BlockItemSwitch(mc.thePlayer.inventory.currentItem));
        }

        clientSlot = -1;
        slot = -1;
    }

    @SubscribeEvent
    public void onPositionRotationUpdate(PosRotUpdateEvent event)
    {
        double posY = mc.thePlayer.boundingBox.minY - 1;

        placements = new ArrayList<>();

        int realSize = size.value() - 1;
        for (int x = -realSize; x <= realSize; x++)
        {
            for (int z = -realSize; z <= realSize; z++)
            {
                double posX = Math.floor(mc.thePlayer.posX) + x;
                double posZ = Math.floor(mc.thePlayer.posZ) + z;
                BlockPair placement = getPlacement(posX, posY, posZ, x == 0 && z == 0);
                if (placement != null)
                    placements.add(placement);
            }
        }

        if (placements.isEmpty()) return;

        placements.sort((a, b) ->
        {
            Vec3D aPos = a.pos();
            double aDeltaX = aPos.xCoord + 0.5 - mc.thePlayer.posX;
            double aDeltaY = aPos.yCoord + 0.5 - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight());
            double aDeltaZ = aPos.zCoord + 0.5 - mc.thePlayer.posZ;
            double aDistanceSq = aDeltaX * aDeltaX + aDeltaY * aDeltaY + aDeltaZ * aDeltaZ;

            Vec3D bPos = b.pos();
            double bDeltaX = bPos.xCoord + 0.5 - mc.thePlayer.posX;
            double bDeltaY = bPos.yCoord + 0.5 - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight());
            double bDeltaZ = bPos.zCoord + 0.5 - mc.thePlayer.posZ;
            double bDistanceSq = bDeltaX * bDeltaX + bDeltaY * bDeltaY + bDeltaZ * bDeltaZ;

            return Double.compare(aDistanceSq, bDistanceSq);
        });

        slot = -1;
        switch (swap.value())
        {
            case Manual:
                if (!isValidBlockStack(getHeldItem())) return;

                slot = mc.thePlayer.inventory.currentItem;
                break;

            case Server:
            case Client:
                slot = getBlockSlot();
                if (slot == -1) return;

                if (swap.value() == Swap.Client)
                {
                    if (clientSlot == -1)
                        clientSlot = mc.thePlayer.inventory.currentItem;

                    mc.thePlayer.inventory.currentItem = slot;
                } else if (swap.value() == Swap.Server)
                {
                    if (clientSlot != slot)
                    {
                        clientSlot = slot;
                        mc.getSendQueue().addToSendQueue(
                                new Packet16BlockItemSwitch(slot));
                    }
                }
                break;
        }

        if (slot == -1) return;

        BlockPair placement = placements.get(0);
        float[] rotations = calculateRotations(placement.pos);
        event.setYaw(rotations[0]);
        event.setPitch(rotations[1]);
        event.setModelRotations();
    }

    @SubscribeEvent
    public void onPositionRotationUpdateFinish(PosRotUpdateFinishEvent event)
    {
        if (placements.isEmpty()) return;
        if (slot == -1) return;

        AtomicBoolean result = new AtomicBoolean(false);
        placements.forEach(placement ->
        {
            boolean temp = mc.playerController.sendPlaceBlock(mc.thePlayer,
                    mc.theWorld,
                    mc.thePlayer.inventory.getStackInSlot(slot),
                    (int) placement.pos.xCoord,
                    (int) placement.pos.yCoord,
                    (int) placement.pos.zCoord,
                    placement.facing.ordinal());

            if (temp)
            {
                if (swing.value())
                {
                    mc.thePlayer.swingItem();
                } else
                {
                    mc.getSendQueue().addToSendQueue(
                            new Packet18Animation(mc.thePlayer, 1));
                }

                result.set(true);
            }
        });
        if (result.get())
        {
            if (this.towerMode.value().equals(Tower.Motion) && Keyboard.isKeyDown(mc.gameSettings.keyBindJump.keyCode))
            {
                mc.thePlayer.motionY = 0.42f;
            }
        }

        if (swap.value() == Swap.Server)
        {
            mc.getSendQueue().addToSendQueue(
                    new Packet16BlockItemSwitch(mc.thePlayer.inventory.currentItem));
        }

        placements.clear();
    }

    @SubscribeEvent
    public void onPlayerMove(PlayerMoveEvent event)
    {
        if (this.safeWalk.value() && mc.thePlayer.onGround)
        {
            double x = event.x();
            double z = event.z();

            EntityPlayerSP player = mc.thePlayer;
            WorldClient world = (WorldClient) mc.theWorld;

            double d6 = 0.05;

            while (x != 0.0 && world.getCollidingBoundingBoxes(player, player.boundingBox.copy().offset(x, -1.0, 0.0)).isEmpty())
            {
                if (x < d6 && x >= -d6)
                {
                    x = 0.0;
                } else if (x > 0.0)
                {
                    x -= d6;
                } else
                {
                    x += d6;
                }
            }

            while (z != 0.0 && world.getCollidingBoundingBoxes(player, player.boundingBox.copy().offset(0.0, -1.0, z)).isEmpty())
            {
                if (z < d6 && z >= -d6)
                {
                    z = 0.0;
                } else if (z > 0.0)
                {
                    z -= d6;
                } else
                {
                    z += d6;
                }
            }

            while (x != 0.0 && z != 0.0 && world.getCollidingBoundingBoxes(player, player.boundingBox.copy().offset(x, -1.0, z)).isEmpty())
            {
                if (x < d6 && x >= -d6)
                {
                    x = 0.0;
                } else if (x > 0.0)
                {
                    x -= d6;
                } else
                {
                    x += d6;
                }

                if (z < d6 && z >= -d6)
                {
                    z = 0.0;
                } else if (z > 0.0)
                {
                    z -= d6;
                } else
                {
                    z += d6;
                }
            }

            event.setX(x);
            event.setZ(z);
        }
    }

    @SubscribeEvent
    public void onPacket(PacketEvent event)
    {
        if (event.packet() instanceof Packet16BlockItemSwitch packet)
        {
            if (packet.id != slot
                    && swap.value() == Swap.Server) event.setCancelled(true);
        }
    }

    private int getBlockSlot()
    {
        if (isValidBlockStack(getHeldItem()))
            return mc.thePlayer.inventory.currentItem;

        int slot = -1, count = 0;
        for (int i = 0; i < 9; ++i)
        {
            ItemStack itemStack = mc.thePlayer.inventory.getStackInSlot(i);
            if (isValidBlockStack(itemStack))
            {
                // auto return infinities because they're well, infinite
                if (understacked.value() && isUnderstacked(itemStack)) return i;

                if (itemStack.stackSize > count)
                {
                    count = itemStack.stackSize;
                    slot = i;
                }
            }
        }

        return slot;
    }

    private float[] calculateRotations(Vec3D target)
    {
        double deltaX = target.xCoord + 0.5 - mc.thePlayer.posX;
        double deltaY = target.yCoord + 0.5 - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight());
        double deltaZ = target.zCoord + 0.5 - mc.thePlayer.posZ;

        double distance = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);

        // calc yaw and pitch with hacker math
        float yaw = (float) (Math.atan2(deltaZ, deltaX) * 180 / Math.PI) - 90;
        float pitch = (float) -(Math.atan2(deltaY, distance) * 180 / Math.PI);

        return new float[]{yaw, pitch};
    }

    private boolean isUnderstacked(ItemStack itemStack)
    {
        return itemStack.stackSize < 1;
    }

    private boolean isValidBlockStack(ItemStack itemStack)
    {
        if (itemStack == null || !(itemStack.getItem() instanceof ItemBlock itemBlock)) return false;

        Block block = Block.blocksList[((ItemBlockAccessor) itemBlock).blockId()];

        return block != null && block.isCollidable() && !INVALID_BLOCKS.contains(block);
    }

    private ItemStack getHeldItem()
    {
        return mc.thePlayer.inventory.getCurrentItem();
    }

    private BlockPair getPlacement(double posX, double posY, double posZ, boolean b)
    {
        Vec3D pos = Vec3D.createVectorHelper(posX, posY, posZ);

        if (!b && !isReplaceable(pos))
            return null;

        for (Facing facing : Facing.values())
        {
            Vec3D neighbor = offset(pos, facing);
            if (!isReplaceable(neighbor)) return new BlockPair(
                    neighbor, opposite(facing));
        }

        if (b)
        {
            for (Facing facing : Facing.values())
            {
                Vec3D neighbor = offset(pos, facing);
                if (isReplaceable(neighbor))
                {
                    for (Facing direction : Facing.values())
                    {
                        Vec3D otherNeighbor = offset(neighbor, direction);
                        if (!isReplaceable(otherNeighbor)) return new BlockPair(
                                otherNeighbor, opposite(direction));
                    }
                }
            }
        }

        return null;
    }

    private Facing opposite(Facing facing)
    {
        return switch (facing)
        {
            case DOWN -> Facing.UP;
            case UP -> Facing.DOWN;
            case NORTH -> Facing.SOUTH;
            case SOUTH -> Facing.NORTH;
            case WEST -> Facing.EAST;
            case EAST -> Facing.WEST;
        };
    }

    private boolean isReplaceable(Vec3D pos)
    {
        return !mc.theWorld.getBlockMaterial(
                (int) pos.xCoord,
                (int) pos.yCoord,
                (int) pos.zCoord).isSolid();
    }

    private Vec3D offset(Vec3D pos, Facing facing)
    {
        return pos.addVector(facing.getFrontOffsetX(),
                facing.getFrontOffsetY(),
                facing.getFrontOffsetZ());
    }

    public static Scaffold instance()
    {
        return instance;
    }

    private record BlockPair(Vec3D pos, Facing facing)
    {
    }

    public enum Tower
    {
        Vanilla,
        Motion
    }

    private enum Swap
    {
        Manual, Client, Server
    }
}
