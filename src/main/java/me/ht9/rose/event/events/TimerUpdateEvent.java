package me.ht9.rose.event.events;

import me.ht9.rose.event.Event;

public final class TimerUpdateEvent extends Event {
    private float timerSpeed = 1.0f;

    public float timerSpeed() {
        return timerSpeed;
    }

    public void setTimerSpeed(float timerSpeed) {
        this.timerSpeed = timerSpeed;
    }
}
