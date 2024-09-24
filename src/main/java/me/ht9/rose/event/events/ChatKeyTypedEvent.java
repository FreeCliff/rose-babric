package me.ht9.rose.event.events;

import me.ht9.rose.event.Event;

public final class ChatKeyTypedEvent extends Event
{
    private final int keyCode;

    private String text;

    public ChatKeyTypedEvent(int keyCode, String text)
    {
        this.keyCode = keyCode;
        this.text = text;
    }

    public int keyCode()
    {
        return this.keyCode;
    }

    public String text()
    {
        return this.text;
    }

    public void setText(String text)
    {
        this.text = text;
    }
}