package me.ht9.rose.util.misc;

import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.GuiTextField;
import org.lwjgl.input.Keyboard;

import static java.net.Proxy.Type;

public final class GuiProxy extends GuiScreen
{
    public static Type type = Type.SOCKS;
    public static String proxyIp = "";

    private final GuiScreen parentScreen;
    private GuiTextField textField;

    public GuiProxy(GuiScreen parentScreen)
    {
        this.parentScreen = parentScreen;
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        controlList.clear();
        controlList.add(new GuiButton(0, this.width / 2 - 100, this.height / 2, "Type: " + type));
        controlList.add(new GuiButton(1, this.width / 2 - 100, this.height / 2 + 22, "Set Proxy"));
        controlList.add(new GuiButton(2, this.width / 2 - 100, this.height / 2 + 44, "Cancel"));

        this.textField = new GuiTextField(this, this.fontRenderer, this.width / 2 - 100, this.height / 2 - 32, 200, 20, proxyIp);
        textField.isFocused = true;
        textField.setMaxStringLength(30);

        ((GuiButton) this.controlList.get(1)).enabled = this.textField.getText().split(":").length == 2 && this.textField.getText().length() > 8;
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    protected void actionPerformed(GuiButton guiButton) {
        if (guiButton.enabled)
        {
            if (guiButton.id == 0)
            {
                if (type.equals(Type.HTTP))
                {
                    type = Type.SOCKS;
                }
                else
                {
                    type = Type.HTTP;
                }
                initGui();
            }
            else if (guiButton.id == 1)
            {
                proxyIp = textField.getText();
                mc.displayGuiScreen(parentScreen);
            }
            else if (guiButton.id == 2)
            {
                mc.displayGuiScreen(parentScreen);
            }
        }
    }

    @Override
    protected void keyTyped(char c, int i) {
        this.textField.textboxKeyTyped(c, i);
        if (c == '\r')
        {
            this.actionPerformed((GuiButton) this.controlList.get(0));
        }

        ((GuiButton) this.controlList.get(1)).enabled = this.textField.getText().split(":").length == 2 && this.textField.getText().length() > 8;
    }

    @Override
    protected void mouseClicked(int i, int j, int k) {
        super.mouseClicked(i, j, k);
        this.textField.mouseClicked(i, j, k);
    }

    @Override
    public void drawScreen(int i, int j, float f) {
        drawDefaultBackground();
        drawCenteredString(fontRenderer, "Proxy", width / 2, 10, -1);
        this.textField.drawTextBox();
        super.drawScreen(i, j, f);
    }
}
