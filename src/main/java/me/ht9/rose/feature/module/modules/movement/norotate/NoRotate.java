package me.ht9.rose.feature.module.modules.movement.norotate;

import me.ht9.rose.event.bus.annotation.SubscribeEvent;
import me.ht9.rose.event.events.PacketEvent;
import me.ht9.rose.feature.module.Module;
import me.ht9.rose.feature.module.annotation.Description;
import net.minecraft.src.Packet10Flying;

@Description("Stops rotation attempts from the server")
public final class NoRotate extends Module
{
    private static final NoRotate instance = new NoRotate();

    @SubscribeEvent
    public void onPacket(PacketEvent event)
    {
        if (event.serverBound()) return;
        if (event.packet() instanceof Packet10Flying packet)
        {
            if (packet.rotating)
            {
                packet.yaw = mc.thePlayer.rotationYaw;
                packet.pitch = mc.thePlayer.rotationPitch;
            }
        }
    }

    public static NoRotate instance()
    {
        return instance;
    }
}
