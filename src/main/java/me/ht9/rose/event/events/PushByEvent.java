package me.ht9.rose.event.events;

import me.ht9.rose.event.Event;
import net.minecraft.src.Entity;

public final class PushByEvent extends Event
{
    private final Pusher pusher;

    private final Entity pushee;

    public PushByEvent(Entity pushee, Pusher pusher)
    {
        this.pushee = pushee;
        this.pusher = pusher;
    }

    public Entity pushee()
    {
        return pushee;
    }

    public Pusher pusher()
    {
        return pusher;
    }

    public enum Pusher
    {
        BLOCK,
        ENTITY,
        LIQUID
    }
}