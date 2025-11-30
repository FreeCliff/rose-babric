package me.ht9.rose.feature.module.modules.misc.retard;

import me.ht9.rose.event.bus.annotation.SubscribeEvent;
import me.ht9.rose.event.events.PosRotUpdateEvent;
import me.ht9.rose.feature.module.Module;
import me.ht9.rose.feature.module.annotation.Description;
import me.ht9.rose.feature.module.setting.Setting;
import net.minecraft.src.Packet18Animation;

import java.util.Random;

@Description("Funny retard mode from Reliant")
public final class Retard extends Module
{
    private static final Retard instance = new Retard();
    private static final Random random = new Random();

    private final Setting<Boolean> swing = new Setting<>("Swing", false);

    @SuppressWarnings("unused")
    @SubscribeEvent
    public void onUpdate(PosRotUpdateEvent event)
    {
        event.setYaw(random.nextFloat(-180, 180));
        event.setPitch(random.nextFloat(-90, 90));
        event.setModelRotations();

        if (mc.thePlayer.ticksExisted % 6 == 0)
        {
            if (swing.value())
            {
                mc.thePlayer.swingItem();
            } else
            {
                mc.getSendQueue().addToSendQueue(new Packet18Animation(mc.thePlayer, 1));
            }
        }
    }

    public static Retard instance()
    {
        return instance;
    }
}
