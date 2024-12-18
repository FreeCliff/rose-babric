package me.ht9.rose.feature.module.modules.misc.autotnt;

import me.ht9.rose.event.bus.annotation.SubscribeEvent;
import me.ht9.rose.event.events.PosRotUpdateEvent;
import me.ht9.rose.feature.module.Module;
import me.ht9.rose.feature.module.annotation.Description;
import me.ht9.rose.feature.module.setting.Setting;
import me.ht9.rose.mixin.accessors.IItemBlock;
import me.ht9.rose.util.world.Facing;
import net.minecraft.src.*;

import java.util.ArrayList;
import java.util.List;

@Description("Automatically places tnt")
public final class AutoTNT extends Module
{
    private static final AutoTNT instance = new AutoTNT();

    private final Setting<Integer> distance = new Setting<>("Distance", 1, 3, 5)
            .withDescription("How far away the TNT should be from each other");
    private final Setting<Swap> swap = new Setting<>("Swap", Swap.Server);

    private final List<Integer> blacklist = new ArrayList<>();

    private int clientSlot = -1, slot = -1;

    @Override
    public void onDisable() {
        blacklist.clear();

        if (clientSlot != -1 && swap.value() == Swap.Client)
        {
            mc.thePlayer.inventory.currentItem = clientSlot;
        }

        mc.getSendQueue().addToSendQueue(new Packet16BlockItemSwitch(mc.thePlayer.inventory.currentItem));

        clientSlot = -1;
        slot = -1;
    }

    @SubscribeEvent
    public void onUpdate(PosRotUpdateEvent event)
    {
        switch (swap.value())
        {
            case Manual:
                if (!isTNT(mc.thePlayer.inventory.getCurrentItem())) return;

                slot = mc.thePlayer.inventory.currentItem;
                break;
            case Server:
            case Client:
                slot = getTNTSlot();
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
                        mc.getSendQueue().addToSendQueue(new Packet16BlockItemSwitch(slot));
                    }
                }
                break;
        }

        if (slot == -1)
            return;

        int dist = distance.value();
        for (int i = -5; i < 5; i++)
        {
            for (int j = -5; j < 5; j++)
            {
                int x = (int) mc.thePlayer.posX - (int) mc.thePlayer.posX % dist - i * dist;
                int z = (int) mc.thePlayer.posZ - (int) mc.thePlayer.posZ % dist - j * dist;

                boolean skip = false;
                for (int l = 0; l < blacklist.size(); l += 2)
                {
                    if (x == blacklist.get(l) && z == blacklist.get(l + 1))
                    {
                        skip = true;
                        break;
                    }
                }

                if (skip) continue;

                for (int k = -5; k < 5; k++)
                {
                    int y = (int) mc.thePlayer.boundingBox.minY + k;
                    if (mc.thePlayer.getDistance(x + 0.5, y + 0.5, z + 0.5) < mc.playerController.getBlockReachDistance())
                    {
                        BlockPair placement = getPlacement(x, y, z);
                        if (placement == null) continue;

                        double deltaX = x + 0.5 - mc.thePlayer.posX;
                        double deltaY = y + 0.5 - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight());
                        double deltaZ = z + 0.5 - mc.thePlayer.posZ;

                        double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);

                        event.setYaw((float) (Math.atan2(deltaZ, deltaX) * 180 / Math.PI) - 90);
                        event.setPitch((float) -(Math.atan2(deltaY, distance) * 180 / Math.PI));
                        event.setModelRotations();

                        boolean result = mc.playerController.sendPlaceBlock(mc.thePlayer,
                                mc.theWorld,
                                mc.thePlayer.inventory.getStackInSlot(slot),
                                (int) placement.pos.xCoord,
                                (int) placement.pos.yCoord,
                                (int) placement.pos.zCoord,
                                placement.facing.ordinal());
                        if (!result) continue;

                        mc.thePlayer.swingItem();

                        blacklist.add(x);
                        blacklist.add(z);
                        break;
                    }
                }
            }
        }
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

    private BlockPair getPlacement(int x, int y, int z)
    {
        Vec3D pos = Vec3D.createVectorHelper(x, y, z);

        for (Facing facing : Facing.values())
        {
            Vec3D neighbor = offset(pos, facing);
            if (!isReplaceable(neighbor)) return new BlockPair(
                    neighbor, opposite(facing));
        }

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

        return null;
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

    private int getTNTSlot()
    {
        if (isTNT(mc.thePlayer.inventory.getCurrentItem()))
        {
            return mc.thePlayer.inventory.currentItem;
        }

        for (int i = 0; i < 9; i++)
        {
            ItemStack itemStack = mc.thePlayer.inventory.getStackInSlot(i);
            if (isTNT(itemStack))
            {
                return i;
            }
        }

        return -1;
    }

    private boolean isTNT(ItemStack itemStack)
    {
        if (itemStack == null || !(itemStack.getItem() instanceof ItemBlock itemBlock)) return false;
        return Block.blocksList[((IItemBlock) itemBlock).blockId()] instanceof BlockTNT;
    }

    public static AutoTNT instance()
    {
        return instance;
    }

    private static class BlockPair
    {
        public Vec3D pos;
        public Facing facing;

        public BlockPair(Vec3D pos, Facing facing)
        {
            this.pos = pos;
            this.facing = facing;
        }
    }

    private enum Swap
    {
        Manual, Client, Server
    }

}
