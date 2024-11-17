package me.ht9.rose.event.events;

import me.ht9.rose.event.Event;
import net.minecraft.src.EntityLiving;

public class RenderPlayerLabelEvent extends Event {
    private final EntityLiving entity;
    private final String name;
    private final double x, y, z;

    public RenderPlayerLabelEvent(EntityLiving entity, String name, double x, double y, double z)
    {
        this.entity = entity;
        this.name = name;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public EntityLiving entity()
    {
        return entity;
    }

    public String name()
    {
        return name;
    }

    public double x()
    {
        return x;
    }

    public double y()
    {
        return y;
    }

    public double z()
    {
        return z;
    }
}
