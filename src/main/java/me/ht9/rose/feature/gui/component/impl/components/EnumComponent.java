package me.ht9.rose.feature.gui.component.impl.components;

import me.ht9.rose.feature.gui.clickgui.RoseGui;
import me.ht9.rose.feature.module.modules.client.clickgui.ClickGUI;
import me.ht9.rose.feature.module.setting.Setting;
import me.ht9.rose.util.render.Render2d;

import java.awt.*;

public final class EnumComponent extends SettingComponent<Enum<?>>
{
    public EnumComponent(Setting<Enum<?>> setting, ModuleComponent parent)
    {
        super(setting, parent);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        float alpha = RoseGui.instance().universalTransparency() / 255.0F;

        Color grey = new Color(15.0F / 255.0F, 15.0F / 255.0F, 15.0F / 255.0F, alpha);

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
        }

        Render2d.drawRect(
                this.x() + 1.5F,
                this.y(),
                this.width() - 3.0F,
                this.height(),
                grey
        );

        Color textGrey = new Color(180.0F / 255.0F, 180.0F / 255.0F, 180.0F / 255.0F, alpha);

        Render2d.drawStringWithShadow(
                this.setting().name(),
                this.x() + 4.0F,
                this.y() + 4.0F,
                new Color(1.0F, 1.0F, 1.0F, alpha),
                ClickGUI.instance().customFont.value()
        );

        Render2d.drawStringWithShadow(
                this.setting().value().name(),
                this.x() + this.width() - Render2d.stringWidth(this.setting().value().name()) - 4.0F,
                this.y() + 4.0F,
                textGrey,
                ClickGUI.instance().customFont.value()
        );
    }

    @Override
    public void onLeftClick(int mouseX, int mouseY)
    {
        if (this.isMouseOverThis(mouseX, mouseY))
        {
            this.playClickSound();
            this.setting().setValue(this.setting().nextEnum());
        }
    }

    @Override
    public void onRightClick(int mouseX, int mouseY)
    {
    }

    @Override
    public void onLeftRelease(int mouseX, int mouseY)
    {
    }

    @Override
    public void onRightRelease(int mouseX, int mouseY)
    {
    }

    @Override
    public void onMiddleClick(int mouseX, int mouseY)
    {
    }

    @Override
    public void onMiddleRelease(int mouseX, int mouseY)
    {
    }

    @Override
    public void onSideButtonClick(int mouseX, int mouseY, SideButton sideButton)
    {
    }

    @Override
    public void onSideButtonRelease(int mouseX, int mouseY, SideButton sideButton)
    {
    }

    @Override
    public void keyTyped(char typedChar, int keyCode)
    {
    }
}