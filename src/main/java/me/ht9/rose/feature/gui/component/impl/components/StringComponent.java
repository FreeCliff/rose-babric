package me.ht9.rose.feature.gui.component.impl.components;

import me.ht9.rose.feature.gui.RoseGui;
import me.ht9.rose.feature.module.setting.Setting;
import me.ht9.rose.util.render.Render2d;
import org.lwjgl.input.Keyboard;

import java.awt.*;

@SuppressWarnings("all")
public final class StringComponent extends SettingComponent<String>
{
    private String textBoxText = "";
    private boolean textBoxFocused = false;

    public StringComponent(Setting<String> setting, ModuleComponent parent)
    {
        super(setting, parent);
        this.textBoxText = setting.value();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        float alpha = RoseGui.instance().universalTransparency() / 255.0F;

        Color grey = new Color(15.0F / 255.0F, 15.0F / 255.0F, 15.0F / 255.0F, alpha);
        if (this.isMouseOverThis(mouseX, mouseY))
        {
            float[] hsb = Color.RGBtoHSB(grey.getRed(), grey.getGreen(), grey.getBlue(), null);
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

        Render2d.drawStringWithShadow(
                this.setting().name(),
                this.x() + 4.0F,
                this.y() + 4.0F,
                new Color(1.0F, 1.0F, 1.0F, alpha)
        );

        int[] bounds = bounds();
        int textBoxX = bounds[0], textBoxY = bounds[1], textBoxWidth = bounds[2], textBoxHeight = bounds[3];

        Render2d.drawStringWithShadow(
                this.textBoxText,
                textBoxX,
                textBoxY,
                new Color(180.0F / 255.0F, 180.0F / 255.0F, 180.0F / 255.0F, alpha)
        );

        if (this.textBoxFocused && System.currentTimeMillis() % 1000 < 500)
        {
            Render2d.drawStringWithShadow(
                    "_",
                    textBoxX + Render2d.stringWidth(this.textBoxText),
                    textBoxY,
                    new Color(255, 255, 255, 255)
            );
        }

        this.setting().setValue(this.textBoxText);
    }

    @Override
    public void onLeftClick(int mouseX, int mouseY)
    {
        int[] bounds = bounds();
        int textBoxX = bounds[0], textBoxY = bounds[1], textBoxWidth = bounds[2], textBoxHeight = bounds[3];

        this.textBoxFocused = mouseX >= textBoxX && mouseX <= textBoxX + textBoxWidth &&
                mouseY >= textBoxY && mouseY <= textBoxY + textBoxHeight;
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
        if (this.textBoxFocused)
        {
            if (keyCode == Keyboard.KEY_BACK)
            {
                if (!this.textBoxText.isEmpty())
                {
                    this.textBoxText = this.textBoxText.substring(0, this.textBoxText.length() - 1);
                }
            } else if (keyCode == Keyboard.KEY_RETURN)
            {
                this.textBoxFocused = false;
            } else if (Character.isLetterOrDigit(typedChar) || typedChar == '-')
            {
                this.textBoxText += typedChar;
            }
        }
    }

    private int[] bounds()
    {
        int textBoxX = (int) (this.x() + 6 + Render2d.stringWidth(this.setting().name()));
        int textBoxY = (int) (this.y() + 4);
        int textBoxWidth = (int) (this.width() - (textBoxX - (int) this.x()) - 10);
        int textBoxHeight = 12;

        return new int[]{textBoxX, textBoxY, textBoxWidth, textBoxHeight};
    }
}