package me.ht9.rose.event.events;

import me.ht9.rose.event.Event;
import net.minecraft.src.Entity;

public final class PushByEvent extends Event
{
    private final Type type;

    private final Entity pushee;

    public PushByEvent(Entity pushee, Type type)
    {
        this.pushee = pushee;
        this.type = type;
    }

    public Entity pushee()
    {
        return pushee;
    }

    public Type type()
    {
        return type;
    }

    public enum Type
    {
        BLOCK,
        ENTITY,
        LIQUID
    }
}