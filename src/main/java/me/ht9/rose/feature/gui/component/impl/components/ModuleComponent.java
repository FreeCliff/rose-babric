package me.ht9.rose.feature.gui.component.impl.components;

import me.ht9.rose.feature.gui.RoseGui;
import me.ht9.rose.feature.gui.component.Component;
import me.ht9.rose.feature.gui.component.impl.windows.ModuleWindow;
import me.ht9.rose.feature.module.keybinding.Bind;
import me.ht9.rose.feature.module.setting.Setting;
import me.ht9.rose.feature.module.Module;
import me.ht9.rose.util.render.Render2d;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public final class ModuleComponent implements Component
{
    private final Module module;
    private final ModuleWindow parent;
    private final float WIDTH = 125.0F;
    private final float HEIGHT = 14.0F;
    private boolean opened = false;
    private float y;
    private final List<SettingComponent<?>> components = new ArrayList<>();

    @SuppressWarnings(value = "unchecked")
    public ModuleComponent(Module module, ModuleWindow parent)
    {
        this.module = module;
        this.parent = parent;
        for (Setting<?> setting : module.settings())
        {
            if (setting.value() instanceof Boolean) {
                this.components.add(new BooleanComponent((Setting<Boolean>) setting, this));
            } else if (setting.value() instanceof String)
            {
            this.components.add(new StringComponent((Setting<String>) setting, this));
            } else if (setting.value() instanceof Enum<?>)
            {
                this.components.add(new EnumComponent((Setting<Enum<?>>) setting, this));
            } else if (setting.value() instanceof Number)
            {
                this.components.add(new NumberComponent((Setting<Number>) setting, this));
            } else if (setting.value() instanceof Bind)
            {
                this.components.add(new BindComponent((Setting<Bind>) setting, this));
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        float alpha = RoseGui.instance().universalTransparency() / 255.0F;

        Color grey = new Color(15.0F / 255.0F, 15.0F / 255.0F, 15.0F / 255.0F, alpha);

        Color enabled = this.defaultColor();

        if (this.isMouseOverThis(mouseX, mouseY))
        {
            float[] hsb = Color.RGBtoHSB(
                    grey.getRed(),
                    grey.getGreen(),
                    grey.getBlue(),
                    null
            );

            hsb[2] = Math.min(hsb[2] + 0.02F, 1.0F);

            grey = Color.getHSBColor(hsb[0], hsb[1], hsb[2]);

            hsb = Color.RGBtoHSB(
                    enabled.getRed(),
                    enabled.getGreen(),
                    enabled.getBlue(),
                    null
            );

            hsb[2] = Math.min(hsb[2] + 0.2F, 1.0F);

            enabled = Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
        }

        Render2d.drawRect(
                this.parent.x(),
                this.y,
                this.WIDTH,
                this.HEIGHT,
                this.module.enabled() ? enabled : grey
        );

        Color textGrey = new Color(180.0F / 255.0F, 180.0F / 255.0F, 180.0F / 255.0F, alpha);

        Render2d.drawStringWithShadow(
                this.module.name(),
                this.parent.x() + 2.0F,
                this.y + 4.0F,
                this.module.enabled() ? new Color(1.0F, 1.0F, 1.0F, alpha) : textGrey
        );

        Render2d.drawStringWithShadow(
                this.opened ? "-" : "+",
                this.parent.x() + this.WIDTH - 13.0F,
                this.y + 3.0F,
                this.module.enabled() ? new Color(1.0F, 1.0F, 1.0F, alpha) : textGrey
        );

        if (this.opened)
        {
            float currY = this.HEIGHT + 1.0F;

            for (SettingComponent<?> component : this.components)
            {
                if (component.setting().visible())
                {
                    component.setY(this.y + currY);
                    component.drawScreen(mouseX, mouseY, partialTicks);
                    currY += component.height() + 1.0F;
                }
            }

            Render2d.drawRect(
                    this.parent.x(),
                    this.y,
                    0.5F,
                    currY - 1.0F,
                    this.module.enabled() ? enabled : grey
            );
        }
    }

    @Override
    public void onLeftClick(int mouseX, int mouseY)
    {
        if (this.isMouseOverThis(mouseX, mouseY))
        {
            this.playClickSound();
            this.module.toggle();
        }

        if (this.opened)
        {
            for (SettingComponent<?> component : this.components)
            {
                if (component.setting().visible())
                {
                    component.onLeftClick(mouseX, mouseY);
                }
            }
        }
    }

    @Override
    public void onRightClick(int mouseX, int mouseY)
    {
        if (this.isMouseOverThis(mouseX, mouseY))
        {
            this.playClickSound();
            this.opened = !this.opened;
        } else if (this.opened)
        {
            for (SettingComponent<?> component : this.components)
            {
                if (component.setting().visible())
                {
                    component.onRightClick(mouseX, mouseY);
                }
            }
        }
    }

    @Override
    public void onLeftRelease(int mouseX, int mouseY)
    {
        if (this.opened)
        {
            for (SettingComponent<?> component : this.components)
            {
                if (component.setting().visible())
                {
                    component.onLeftRelease(mouseX, mouseY);
                }
            }
        }
    }

    @Override
    public void onRightRelease(int mouseX, int mouseY)
    {
        if (this.opened)
        {
            for (SettingComponent<?> component : this.components)
            {
                if (component.setting().visible())
                {
                    component.onRightRelease(mouseX, mouseY);
                }
            }
        }
    }

    @Override
    public void onMiddleClick(int mouseX, int mouseY)
    {
        if (this.opened)
        {
            for (SettingComponent<?> component : this.components)
            {
                if (component.setting().visible())
                {
                    component.onMiddleClick(mouseX, mouseY);
                }
            }
        }
    }

    @Override
    public void onMiddleRelease(int mouseX, int mouseY)
    {
        if (this.opened)
        {
            for (SettingComponent<?> component : this.components)
            {
                if (component.setting().visible())
                {
                    component.onMiddleRelease(mouseX, mouseY);
                }
            }
        }
    }

    @Override
    public void onSideButtonClick(int mouseX, int mouseY, SideButton sideButton)
    {
        if (this.opened)
        {
            for (SettingComponent<?> component : this.components)
            {
                if (component.setting().visible())
                {
                    component.onSideButtonClick(mouseX, mouseY, sideButton);
                }
            }
        }
    }

    @Override
    public void onSideButtonRelease(int mouseX, int mouseY, SideButton sideButton)
    {
        if (this.opened)
        {
            for (SettingComponent<?> component : this.components)
            {
                if (component.setting().visible())
                {
                    component.onSideButtonRelease(mouseX, mouseY, sideButton);
                }
            }
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode)
    {
        if (this.opened)
        {
            for (SettingComponent<?> component : this.components)
            {
                if (component.setting().visible())
                {
                    component.keyTyped(typedChar, keyCode);
                }
            }
        }
    }

    public boolean isMouseOverThis(float mouseX, float mouseY)
    {
        return mouseX >= this.parent.x()
                && mouseX <= this.parent.x() + this.WIDTH
                && mouseY > this.y
                && mouseY <= this.y + this.HEIGHT;
    }

    public float totalHeight()
    {
        float totalHeight = this.HEIGHT + 1.0F;
        if (this.opened)
        {
            for (SettingComponent<?> component : this.components)
            {
                if (component.setting().visible())
                {
                    totalHeight += component.height() + 1.0F;
                }
            }
        }
        return totalHeight;
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

    public Module module()
    {
        return this.module;
    }

    public List<SettingComponent<?>> components() {
        return components;
    }
}