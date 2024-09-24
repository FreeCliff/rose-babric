package me.ht9.rose.event.events;

import me.ht9.rose.event.Event;

public class InputEvent extends Event
{
    public static class MouseInputEvent extends InputEvent {}
    public static class KeyInputEvent extends InputEvent {}
}