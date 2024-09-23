package me.ht9.rose.event.events;

import me.ht9.rose.event.Event;
import net.minecraft.src.ScaledResolution;

public class RenderGameOverlayEvent extends Event
{
    private final float partialTicks;
    private final ScaledResolution resolution;

    public RenderGameOverlayEvent(float partialTicks, ScaledResolution resolution)
    {
        this.partialTicks = partialTicks;
        this.resolution = resolution;
    }

    public ScaledResolution getResolution()
    {
        return resolution;
    }
    public float getPartialTicks()
    {
        return partialTicks;
    }
}