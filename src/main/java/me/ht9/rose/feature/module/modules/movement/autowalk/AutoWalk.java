package me.ht9.rose.feature.module.modules.movement.autowalk;

import me.ht9.rose.event.bus.annotation.SubscribeEvent;
import me.ht9.rose.event.events.MoveStateUpdateEvent;
import me.ht9.rose.feature.module.Module;

@SuppressWarnings("unused")
public final class AutoWalk extends Module
{
    private static final AutoWalk instance = new AutoWalk();

    @SubscribeEvent
    public void onUpdate(MoveStateUpdateEvent event)
    {
        event.setMoveForward(true);
    }

    public static AutoWalk instance()
    {
        return instance;
    }
}
