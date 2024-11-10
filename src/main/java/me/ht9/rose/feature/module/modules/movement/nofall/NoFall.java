package me.ht9.rose.feature.module.modules.movement.nofall;

import me.ht9.rose.event.bus.annotation.SubscribeEvent;
import me.ht9.rose.event.events.PosRotUpdateEvent;
import me.ht9.rose.feature.module.Module;
import me.ht9.rose.feature.module.annotation.Description;

@Description("removes fall damage")
public final class NoFall extends Module {
    private static final NoFall instance = new NoFall();

    @SuppressWarnings("unused")
    @SubscribeEvent
    public void onUpdate(PosRotUpdateEvent event) {
        event.setOnGround(true);
    }

    public static NoFall instance() {
        return instance;
    }
}
