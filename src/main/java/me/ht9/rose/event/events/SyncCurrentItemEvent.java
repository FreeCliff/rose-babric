package me.ht9.rose.event.events;

import me.ht9.rose.event.Event;

public final class SyncCurrentItemEvent extends Event
{
    private final Type type;

    public SyncCurrentItemEvent(Type type)
    {
        this.type = type;
    }

    public Type type() {
        return type;
    }

    public enum Type
    {
        BREAK,
        PLACE,
        USE_ITEM,
        ATTACK,
        INTERACT_ENTITY
    }
}
