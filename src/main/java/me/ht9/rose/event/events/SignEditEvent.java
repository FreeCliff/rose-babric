package me.ht9.rose.event.events;

import me.ht9.rose.event.Event;
import net.minecraft.src.TileEntitySign;

public final class SignEditEvent extends Event
{
    private final Type type;
    private final TileEntitySign sign;

    private String line1;
    private String line2;
    private String line3;
    private String line4;

    public SignEditEvent(Type type, TileEntitySign sign)
    {
        this.type = type;
        this.sign = sign;
    }

    public Type type()
    {
        return type;
    }

    public String line1()
    {
        return line1;
    }

    public void setLine1(String line1)
    {
        this.line1 = line1;
    }

    public String line2()
    {
        return line2;
    }

    public void setLine2(String line2)
    {
        this.line2 = line2;
    }

    public String line3()
    {
        return line3;
    }

    public void setLine3(String line3)
    {
        this.line3 = line3;
    }

    public String line4()
    {
        return line4;
    }

    public void setLine4(String line4)
    {
        this.line4 = line4;
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
