package me.ht9.rose.feature.module.modules.misc.timer;

import me.ht9.rose.event.bus.annotation.SubscribeEvent;
import me.ht9.rose.event.events.TickEvent;
import me.ht9.rose.feature.module.Module;
import me.ht9.rose.feature.module.annotation.Description;
import me.ht9.rose.feature.module.setting.Setting;
import me.ht9.rose.mixin.accessors.MinecraftAccessor;

@Description("Change your game speed.")
public final class Timer extends Module
{
    private static final Timer instance = new Timer();

    public final Setting<Float> timer = new Setting<>("Timer", 1f, 1f, 25f, 2);

    private Timer()
    {
        setArrayListInfo(() -> timer.value().toString());
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public void onTick(TickEvent event)
    {
        ((MinecraftAccessor) mc).timer().timerSpeed = timer.value();
    }

    @Override
    public void onDisable()
    {
        ((MinecraftAccessor) mc).timer().timerSpeed = 1.0f;
    }

    public static Timer instance()
    {
        return instance;
    }
}
