package me.ht9.rose.feature.module.modules.movement.velocity;

import me.ht9.rose.event.bus.annotation.SubscribeEvent;
import me.ht9.rose.event.events.PacketEvent;
import me.ht9.rose.event.events.PushByEvent;
import me.ht9.rose.feature.module.Module;
import me.ht9.rose.feature.module.annotation.Description;
import me.ht9.rose.feature.module.setting.Setting;
import net.minecraft.src.*;

@Description("Cancels knockback from entities, liquids, and blocks.")
public class Velocity extends Module
{
    private static final Velocity instance = new Velocity();

    private final Setting<Boolean> noPush = new Setting<>("NoPush", true);
    private final Setting<Boolean> blocks = new Setting<>("Blocks", true, this.noPush::value);
    private final Setting<Boolean> liquids   = new Setting<>("Liquids", true, this.noPush::value);
    private final Setting<Boolean> entities  = new Setting<>("Entities", true, this.noPush::value);

    @SuppressWarnings("unused")
    @SubscribeEvent
    public void onPacket(PacketEvent event)
    {
        if (mc.thePlayer == null || mc.theWorld == null) return;

        if (!event.serverBound())
        {
            if (event.packet() instanceof Packet28EntityVelocity packet)
            {
                if (packet.entityId == mc.thePlayer.entityId)
                {
                    event.setCancelled(true);
                }
            } else if (event.packet() instanceof Packet60Explosion packet)
            {
                event.setCancelled(true);
                Explosion explosion = new Explosion(mc.theWorld, null, packet.explosionX, packet.explosionY, packet.explosionZ, packet.explosionSize);
                explosion.doExplosionB(true);
            }
        }
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public void onPushOutOfBlocks(PushByEvent event)
    {
        if (this.noPush.value() && event.pushee().equals(mc.thePlayer))
        {
            switch (event.pusher())
            {
                case BLOCK ->
                {
                    if (blocks.value())
                    {
                        if (event.pusher().equals(PushByEvent.Pusher.BLOCK))
                        {
                            event.setCancelled(true);
                        }
                    }
                }
                case ENTITY ->
                {
                    if (entities.value())
                    {
                        if (event.pusher().equals(PushByEvent.Pusher.ENTITY))
                        {
                            event.setCancelled(true);
                        }
                    }
                }
                case LIQUID ->
                {
                    if (liquids.value())
                    {
                        if (event.pusher().equals(PushByEvent.Pusher.LIQUID))
                        {
                            event.setCancelled(true);
                        }
                    }
                }
                default -> throw new IllegalStateException("Unexpected value: " + event.pusher());
            }
        }
    }

    public static Velocity instance()
    {
        return instance;
    }
}