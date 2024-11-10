package me.ht9.rose.event.events;

import me.ht9.rose.event.Event;

public final class PlayerDamageBlockEvent extends Event
{
    private final int x, y, z, facing;

    public PlayerDamageBlockEvent(int x, int y, int z, int facing)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.facing = facing;
    }

    public int x()
    {
        return this.x;
    }

    public int y()
    {
        return this.y;
    }

    public int z()
    {
        return this.z;
    }

    public int facing()
    {
        return this.facing;
    }
}