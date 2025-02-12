package me.ht9.rose.event.events;

import me.ht9.rose.event.Event;
import net.minecraft.src.TileEntitySign;

public final class SignEditEvent extends Event
{
    private final Type type;
    private final TileEntitySign sign;

    public SignEditEvent(Type type, TileEntitySign sign)
    {
        this.type = type;
        this.sign = sign;
    }

    public Type type()
    {
        return type;
    }

    public TileEntitySign sign()
    {
        return sign;
    }

    public enum Type
    {
        START,
        FINISH
    }
}
