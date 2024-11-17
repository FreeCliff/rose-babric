package me.ht9.rose.event.events;

import me.ht9.rose.event.Event;

public final class FOVModifierEvent extends Event
{
    private float fov = -1;

    public float fov() {
        return fov;
    }

    public void setFov(float fov) {
        this.fov = fov;
    }
}
