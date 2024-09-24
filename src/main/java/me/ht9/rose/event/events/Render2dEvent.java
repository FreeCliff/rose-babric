package me.ht9.rose.event.events;

import me.ht9.rose.event.Event;

public class Render2dEvent extends Event
{
    private final float partialTicks;

    public Render2dEvent(float partialTicks)
    {
        this.partialTicks = partialTicks;
    }
    
    public float partialTicks()
    {
        return partialTicks;
    }
}