package me.ht9.rose.feature.module.modules.render.fov;

import me.ht9.rose.event.bus.annotation.SubscribeEvent;
import me.ht9.rose.event.events.FOVModifierEvent;
import me.ht9.rose.feature.module.Module;
import me.ht9.rose.feature.module.setting.Setting;

public final class FOV extends Module
{
    private static final FOV instance = new FOV();

    private final Setting<Integer> fov = new Setting<>("FOV", 30, 90, 125);

    @SuppressWarnings("unused")
    @SubscribeEvent
    public void onFOV(FOVModifierEvent event)
    {
        event.setFov(fov.value());
    }

    public static FOV instance()
    {
        return instance;
    }
}
