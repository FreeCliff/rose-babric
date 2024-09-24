package me.ht9.rose.feature.gui.component;

import me.ht9.rose.feature.gui.RoseGui;
import me.ht9.rose.util.Globals;

public abstract class DraggableComponent implements Component, Globals
{
    private float x;
    private float y;

    public DraggableComponent(float x, float y)
    {
        this.x = x;
        this.y = y;
    }

    public void drag(float mouseX, float mouseY)
    {
        this.x += mouseX - RoseGui.instance().lastMouseX();
        this.y += mouseY - RoseGui.instance().lastMouseY();
    }

    public float x()
    {
        return this.x;
    }

    public float y()
    {
        return this.y;
    }

    public void setX(float x)
    {
        this.x = x;
    }

    public void setY(float y)
    {
        this.y = y;
    }
}