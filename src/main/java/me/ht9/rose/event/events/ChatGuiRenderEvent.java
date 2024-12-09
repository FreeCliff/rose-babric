package me.ht9.rose.event.events;

import me.ht9.rose.event.Event;

public final class ChatGuiRenderEvent extends Event
{
    private String text;

    public ChatGuiRenderEvent(String text)
    {
        this.text = text;
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