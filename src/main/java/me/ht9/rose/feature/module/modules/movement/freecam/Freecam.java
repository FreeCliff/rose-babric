package me.ht9.rose.feature.module.modules.movement.freecam;

import me.ht9.rose.event.bus.annotation.SubscribeEvent;
import me.ht9.rose.event.events.PacketEvent;
import me.ht9.rose.event.events.PosRotUpdateEvent;
import me.ht9.rose.event.events.PushByEvent;
import me.ht9.rose.feature.module.annotation.Description;
import me.ht9.rose.feature.module.Module;
import me.ht9.rose.feature.module.setting.Setting;
import me.ht9.rose.util.module.Movement;
import net.minecraft.src.*;
import org.lwjgl.input.Keyboard;

@Description("Camera for free.")
public class Freecam extends Module
{
    private static final Freecam instance = new Freecam();

    private final Setting<Float> speed = new Setting<>("Speed", 0.1f, 2.5f, 5.0f, 1);

    private Entity entity;
    private double x, y, z;
    private float yaw, pitch;

    private Freecam()
    {
        setArrayListInfo(() -> speed.value().toString());
    }

    @Override
    public void onEnable()
    {
        if(mc.thePlayer == null || mc.theWorld == null || !mc.isMultiplayerWorld()) return;

        EntityOtherPlayerMP entity = new EntityOtherPlayerMP(mc.thePlayer.worldObj, mc.session.username);
        this.copy(mc.thePlayer);

        entity.setPositionAndRotation(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch);
        entity.inventory = mc.thePlayer.inventory;

        x = mc.thePlayer.posX;
        y = mc.thePlayer.posY;
        z = mc.thePlayer.posZ;
        yaw = mc.thePlayer.rotationYaw;
        pitch = mc.thePlayer.rotationPitch;

        entity.posY -= mc.thePlayer.yOffset;
        this.entity = entity;
        mc.theWorld.joinEntityInSurroundings(entity);
    }

    @Override
    public void onDisable()
    {
        if(entity != null)
            mc.theWorld.setEntityDead(entity);

        Movement.setSpeed(0);
        mc.thePlayer.setPosition(x, y, z);
        mc.thePlayer.setVelocity(0, 0, 0);
        mc.thePlayer.rotationYaw = yaw;
        mc.thePlayer.rotationPitch = pitch;
        mc.thePlayer.noClip = false;
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public void onPacket(PacketEvent event)
    {
        if(mc.thePlayer == null || mc.theWorld == null || mc.currentScreen instanceof GuiDownloadTerrain)
            return;

        if (!event.serverBound()) return;

        if(event.packet() instanceof Packet10Flying || event.packet() instanceof Packet11PlayerPosition || event.packet() instanceof Packet12PlayerLook || event.packet() instanceof Packet13PlayerLookMove)
        {
            mc.getSendQueue().addToSendQueue(new Packet0KeepAlive()); // this is to prevent the server from kicking us :pray:
            event.setCancelled(true);
        }
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public void onUpdate(PosRotUpdateEvent event)
    {
        mc.thePlayer.noClip = true;
        mc.thePlayer.motionY = 0.0;

        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE) && mc.currentScreen == null)
        {
            mc.thePlayer.motionY += 0.55;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) && mc.currentScreen == null)
        {
            mc.thePlayer.motionY -= 0.65;
        }

        Movement.setSpeed(
                mc.thePlayer.movementInput.moveForward != 0 || mc.thePlayer.movementInput.moveStrafe != 0
                        ? speed.value()
                        : 0
        );
    }

    @SubscribeEvent
    public void onPush(PushByEvent event)
    {
        event.setCancelled(true);
    }

    public void copy(Entity entity)
    {
        NBTTagCompound nbt = new NBTTagCompound();
        entity.writeToNBT(nbt);
        entity.readFromNBT(nbt);
    }


    public static Freecam instance()
    {
        return instance;
    }
}