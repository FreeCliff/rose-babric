package me.ht9.rose.feature.module.modules.misc.retard;

import me.ht9.rose.event.bus.annotation.SubscribeEvent;
import me.ht9.rose.event.events.PosRotUpdateEvent;
import me.ht9.rose.feature.module.Module;
import me.ht9.rose.feature.module.annotation.Description;

import java.util.Random;

@Description("Funny retard mode inspired from Reliant")
public final class Retard extends Module
{
    private static final Retard instance = new Retard();
    private static final Random random = new Random();

    @SuppressWarnings("unused")
    @SubscribeEvent
    public void onUpdate(PosRotUpdateEvent event)
    {
        event.setYaw(random.nextFloat(-180, 180));
        event.setPitch(random.nextFloat(-90, 90));
        event.setModelRotations();

        if (mc.thePlayer.ticksExisted % 6 == 0)
            mc.thePlayer.swingItem();
    }

    public static Retard instance()
    {
        return instance;
    }
}
