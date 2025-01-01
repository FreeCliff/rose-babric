package me.ht9.rose.feature.gui.component;

import me.ht9.rose.feature.gui.clickgui.RoseGui;
import me.ht9.rose.feature.module.modules.client.clickgui.ClickGUI;
import me.ht9.rose.util.Globals;

import java.awt.*;

public interface Component extends Globals
{
    void drawScreen(int mouseX, int mouseY, float partialTicks);

    void onLeftClick(int mouseX, int mouseY);

    void onRightClick(int mouseX, int mouseY);

    void onLeftRelease(int mouseX, int mouseY);

    void onRightRelease(int mouseX, int mouseY);

    void onMiddleClick(int mouseX, int mouseY);

    void onMiddleRelease(int mouseX, int mouseY);

    void onSideButtonClick(int mouseX, int mouseY, SideButton sideButton);

    void onSideButtonRelease(int mouseX, int mouseY, SideButton sideButton);

    void keyTyped(char typedChar, int keyCode);

    default void playClickSound()
    {
        mc.sndManager.playSoundFX("gui.button.press", 1.0F, 0.1F);
    }

    default Color defaultColor()
    {
        return new Color(
                ClickGUI.instance().red.value() / 255.0F,
                ClickGUI.instance().green.value() / 255.0F,
                ClickGUI.instance().blue.value() / 255.0F,
                RoseGui.instance().universalTransparency() / 255.0F
        );
    }

    enum SideButton
    {
        SIDE1(3),
        SIDE2(4);

        private final int code;

        SideButton(int code)
        {
            this.code = code;
        }

        public int code()
        {
            return this.code;
        }
    }
}