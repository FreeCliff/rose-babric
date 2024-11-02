package me.ht9.rose.event.events;

import me.ht9.rose.event.Event;

public final class RenderOverlayEvent extends Event
{
    private final Overlay overlay;

    public RenderOverlayEvent(Overlay overlay)
    {
        this.overlay = overlay;
    }

    public Overlay overlay()
    {
        return overlay;
    }

    public enum Overlay
    {
        FIRE,
        BLOCKS,
        HAND
    }
}