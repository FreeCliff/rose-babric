package me.ht9.rose.event.events;

import me.ht9.rose.event.Event;

public class RenderWorldEvent extends Event
{
    private final Type type;

    public RenderWorldEvent(Type type)
    {
        this.type = type;
    }

    public Type type()
    {
        return type;
    }

    public enum Type
    {
        CLOUD,
        FOG,
        HURTCAM,
        RAIN,
        VIEWBOB
    }
}
