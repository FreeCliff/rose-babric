package me.ht9.rose.event.events;

import me.ht9.rose.event.Event;

public final class PlayerMoveEvent extends Event
{
    private double x;
    private double y;
    private double z;

    public PlayerMoveEvent(double x, double y, double z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double x()
    {
        return this.x;
    }

    public double y()
    {
        return this.y;
    }

    public double z()
    {
        return this.z;
    }

    public void setX(double x)
    {
        this.x = x;
    }

    public void setY(double y)
    {
        this.y = y;
    }

    public void setZ(double z)
    {
        this.z = z;
    }
}