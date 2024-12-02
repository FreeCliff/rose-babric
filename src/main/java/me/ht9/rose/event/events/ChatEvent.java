package me.ht9.rose.event.events;

import me.ht9.rose.event.Event;

public final class ChatEvent extends Event
{
    private String message;

    public ChatEvent(String message)
    {
        this.message = message;
    }

    public String message()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }
}
