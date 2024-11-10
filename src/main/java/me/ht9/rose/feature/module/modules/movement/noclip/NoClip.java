package me.ht9.rose.feature.module.modules.movement.noclip;

import me.ht9.rose.event.bus.annotation.SubscribeEvent;
import me.ht9.rose.event.events.PosRotUpdateEvent;
import me.ht9.rose.feature.module.Module;
import me.ht9.rose.feature.module.annotation.Description;

@Description("clip through blocks or something")
public final class NoClip extends Module {
    private static final NoClip instance = new NoClip();

    @Override
    public void onDisable() {
        mc.thePlayer.noClip = false;
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public void onUpdate(PosRotUpdateEvent event) {
        if (mc.thePlayer == null || mc.theWorld == null) return;
        mc.thePlayer.noClip = true;
    }

    public static NoClip instance() {
        return instance;
    }
}
