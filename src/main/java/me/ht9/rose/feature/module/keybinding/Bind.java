package me.ht9.rose.feature.module.keybinding;

public final class Bind
{
    private int key;
    private BindType type;
    private Runnable action;

    public Bind(int key, BindType type)
    {
        this.key = key;
        this.type = type;
    }

    public Bind withAction(Runnable action)
    {
        this.action = action;
        return this;
    }

    public void runAction()
    {
        action.run();
    }

    public int key()
    {
        return this.key;
    }

    public BindType type()
    {
        return this.type;
    }

    public void setKey(int key, BindType type)
    {
        this.key = key;
        this.type = type;
    }

    public enum BindType
    {
        KEYBOARD,
        MOUSE
    }
}