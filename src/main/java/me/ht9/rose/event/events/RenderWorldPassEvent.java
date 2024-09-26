package me.ht9.rose.event.events;

import me.ht9.rose.event.Event;

public final class RenderWorldPassEvent extends Event
{
    private final float partialTicks;

    public RenderWorldPassEvent(float partialTicks)
    {
        this.partialTicks = partialTicks;
    }

    public float partialTicks()
    {
        return this.partialTicks;
    }
}