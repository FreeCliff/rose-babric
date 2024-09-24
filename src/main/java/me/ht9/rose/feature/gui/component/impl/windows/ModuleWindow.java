package me.ht9.rose.feature.gui.component.impl.windows;

import me.ht9.rose.feature.gui.RoseGui;
import me.ht9.rose.feature.gui.component.DraggableComponent;
import me.ht9.rose.feature.gui.component.impl.components.ModuleComponent;
import me.ht9.rose.feature.module.modules.Category;
import me.ht9.rose.feature.module.Module;
import me.ht9.rose.util.render.Render2d;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public final class ModuleWindow extends DraggableComponent
{
    private final float WIDTH = 125.0F;
    private final float HEIGHT = 14.0F;
    private final Category category;
    private boolean opened = true;
    private final List<ModuleComponent> components = new ArrayList<>();

    public ModuleWindow(float x, float y, Category category)
    {
        super(x, y);
        this.category = category;
        for (Module module : category.modules())
        {
            this.components.add(new ModuleComponent(module, this));
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        float alpha = RoseGui.instance().universalTransparency() / 255.0F;

        // top
        Render2d.drawRect(
                this.x() - 2.0F,
                this.y() - 2.0F,
                this.WIDTH + 4.0F,
                0.5F,
                new Color(1.0F, 1.0F, 1.0F, alpha)
        );

        // right
        Render2d.drawRect(
                this.x() - 2.0F,
                this.y() - 2.0F,
                0.5F,
                this.getTotalHeight() + 1.0F,
                new Color(1.0F, 1.0F, 1.0F, alpha)
        );

        // left
        Render2d.drawRect(
                this.x() + this.WIDTH + 1.5F,
                this.y() - 2.0F,
                0.5F,
                this.getTotalHeight() + 1.0F,
                new Color(1.0F, 1.0F, 1.0F, alpha)
        );

        // bottom
        Render2d.drawRect(
                this.x() - 2.0F,
                this.y() + this.getTotalHeight() - 1.5F,
                this.WIDTH + 4.0F,
                0.5F,
                new Color(1.0F, 1.0F, 1.0F, alpha)
        );

        Color grey = new Color(25.0F / 255.0F, 25.0F / 255.0F, 25.0F / 255.0F, alpha);

        Render2d.drawRect(
                this.x() - 1.5F,
                this.y() - 1.5F,
                this.WIDTH + 3.0F,
                this.getTotalHeight(),
                grey
        );

        Color black = new Color(5.0F / 255.0F, 5.0F / 255.0F, 5.0F / 255.0F, alpha);

        if (this.isMouseOverTitle(mouseX, mouseY))
        {
            float[] hsb = Color.RGBtoHSB(
                    black.getRed(),
                    black.getGreen(),
                    black.getBlue(),
                    null
            );

            hsb[2] = Math.min(hsb[2] + 0.05F, 1.0F);

            black = Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
        }

        Render2d.drawRect(
                this.x() - 1.0F,
                this.y() - 1.0F,
                this.WIDTH + 1.0F,
                this.HEIGHT + 1.0F,
                black
        );

        Render2d.drawStringWithShadow(
                this.category.name(),
                this.x() + 2.0F,
                this.y() + 3.0F,
                new Color(1.0F, 1.0F, 1.0F, alpha)
        );

        if (this.opened)
        {
            float currY = this.HEIGHT + 2.0F;
            for (ModuleComponent component : this.components)
            {
                component.setY(this.y() + currY);
                component.drawScreen(mouseX, mouseY, partialTicks);
                currY += component.getTotalHeight();
            }
        }
    }

    @Override
    public void onLeftClick(int mouseX, int mouseY)
    {
        if (this.opened)
        {
            this.components.forEach(
                    c -> c.onLeftClick(mouseX, mouseY)
            );
        }
    }

    @Override
    public void onRightClick(int mouseX, int mouseY)
    {
        if (this.isMouseOverTitle(mouseX, mouseY))
        {
            this.playClickSound();
            this.opened = !this.opened;
        } else if (this.opened)
        {
            this.components.forEach(
                    c -> c.onRightClick(mouseX, mouseY)
            );
        }
    }

    @Override
    public void onLeftRelease(int mouseX, int mouseY)
    {
        if (this.opened)
        {
            this.components.forEach(
                    c -> c.onLeftRelease(mouseX, mouseY)
            );
        }
    }

    @Override
    public void onRightRelease(int mouseX, int mouseY)
    {
        if (this.opened)
        {
            this.components.forEach(
                    c -> c.onRightRelease(mouseX, mouseY)
            );
        }
    }

    @Override
    public void onMiddleClick(int mouseX, int mouseY)
    {
        if (this.opened)
        {
            this.components.forEach(
                    c -> c.onMiddleClick(mouseX, mouseY)
            );
        }
    }

    @Override
    public void onMiddleRelease(int mouseX, int mouseY)
    {
        if (this.opened)
        {
            this.components.forEach(
                    c -> c.onMiddleRelease(mouseX, mouseY)
            );
        }
    }

    @Override
    public void onSideButtonClick(int mouseX, int mouseY, SideButton sideButton)
    {
        if (this.opened)
        {
            this.components.forEach(
                    c -> c.onSideButtonClick(mouseX, mouseY, sideButton)
            );
        }
    }

    @Override
    public void onSideButtonRelease(int mouseX, int mouseY, SideButton sideButton)
    {
        if (this.opened)
        {
            this.components.forEach(
                    c -> c.onSideButtonRelease(mouseX, mouseY, sideButton)
            );
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode)
    {
        if (this.opened)
        {
            this.components.forEach(
                    c -> c.keyTyped(typedChar, keyCode)
            );
        }
    }

    public boolean isMouseOverTitle(float mouseX, float mouseY)
    {
        return mouseX >= this.x()
                && mouseX <= this.x() + this.WIDTH
                && mouseY >= this.y()
                && mouseY <= this.y() + this.HEIGHT;
    }

    public boolean isMouseOverWholeTab(float mouseX, float mouseY)
    {
        return mouseX >= this.x()
                && mouseX <= this.x() + this.WIDTH
                && mouseY >= this.y()
                && mouseY <= this.y() + this.getTotalHeight();
    }

    private float getTotalHeight()
    {
        float totalHeight = this.HEIGHT + 4.0F;
        if (this.opened)
        {
            for (ModuleComponent component : this.components)
            {
                totalHeight += component.getTotalHeight();
            }
        }
        return totalHeight;
    }

    public String getName()
    {
        return this.category.name();
    }

    public boolean isOpened()
    {
        return this.opened;
    }

    public void setOpened(boolean opened)
    {
        this.opened = opened;
    }

    public List<ModuleComponent> components()
    {
        return this.components;
    }
}