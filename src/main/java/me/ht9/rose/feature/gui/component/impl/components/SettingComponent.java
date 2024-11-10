package me.ht9.rose.feature.gui.component.impl.components;

import me.ht9.rose.feature.gui.component.Component;
import me.ht9.rose.feature.module.setting.Setting;

public abstract class SettingComponent<V> implements Component
{
    private final Setting<V> setting;
    private final ModuleComponent parent;
    private float y;

    public SettingComponent(Setting<V> setting, ModuleComponent parent)
    {
        this.setting = setting;
        this.parent = parent;
    }

    public boolean isMouseOverThis(float mouseX, float mouseY)
    {
        return mouseX >= this.x() && mouseX <= this.x() + this.width() && mouseY > this.y() && mouseY <= this.y() + this.height();
    }

    public Setting<V> setting()
    {
        return this.setting;
    }

    public float width()
    {
        return 125.0F;
    }

    public float height()
    {
        return 14.0F;
    }

    public float x()
    {
        return this.parent.x();
    }

    public float y()
    {
        return this.y;
    }

    public void setY(float y)
    {
        this.y = y;
    }
}