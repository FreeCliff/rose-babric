package me.ht9.rose.event.events;

import me.ht9.rose.event.Event;
import me.ht9.rose.feature.module.Module;

public final class ModuleEvent extends Event
{
    private final Module module;
    private final Type type;

    public ModuleEvent(Module module, Type type)
    {
        this.module = module;
        this.type = type;
    }

    public Module module()
    {
        return this.module;
    }

    public Type type()
    {
        return this.type;
    }

    public enum Type
    {
        ENABLE,
        DISABLE,
        DRAW,
        UNDRAW
    }
}
