package me.ht9.rose.feature.module.modules.movement.step;

import me.ht9.rose.event.bus.annotation.SubscribeEvent;
import me.ht9.rose.event.events.PosRotUpdateEvent;
import me.ht9.rose.feature.module.Module;
import me.ht9.rose.feature.module.annotation.Description;

@Description("Lets you step")
public final class Step extends Module
{
    private static final Step instance = new Step();

    @Override
    public void onDisable()
    {
        mc.thePlayer.stepHeight = 0.5f;
    }

    @SubscribeEvent
    public void onUpdate(PosRotUpdateEvent event)
    {
        if (mc.thePlayer == null) return;
        mc.thePlayer.stepHeight = 1.0f;
    }

    public static Step instance()
    {
        return instance;
    }
}
