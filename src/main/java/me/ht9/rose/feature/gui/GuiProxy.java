package me.ht9.rose.feature.gui;

import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.GuiTextField;
import org.lwjgl.input.Keyboard;

import static java.net.Proxy.Type;

public final class GuiProxy extends GuiScreen
{
    public static Type type = Type.SOCKS;
    public static String proxyIp = "";
    public static String proxyAuth = "";

    private final GuiScreen parentScreen;
    private GuiTextField ipField;
    private GuiTextField authField;

    public GuiProxy(GuiScreen parentScreen)
    {
        this.parentScreen = parentScreen;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        controlList.clear();
        controlList.add(new GuiButton(0, this.width / 2 - 100, this.height / 2 + 30, "Type: " + type));
        controlList.add(new GuiButton(1, this.width / 2 - 100, this.height / 2 + 52, "Set Proxy"));
        controlList.add(new GuiButton(2, this.width / 2 - 100, this.height / 2 + 74, "Cancel"));

        this.ipField = new GuiTextField(this, this.fontRenderer, this.width / 2 - 100, this.height / 2 - 32, 200, 20, proxyIp);
        ipField.isFocused = true;
        ipField.setMaxStringLength(100);

        this.authField = new GuiTextField(this, this.fontRenderer, this.width / 2 - 100, this.height / 2 - 8, 200, 20, proxyAuth);
        authField.setMaxStringLength(100);

        ((GuiButton) this.controlList.get(1)).enabled = this.ipField.getText().isEmpty() || (this.ipField.getText().split(":").length == 2 && this.ipField.getText().length() > 8);
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
                ((GuiButton) controlList.get(0)).displayString = "Type: " + type;
            }
            else if (guiButton.id == 1)
            {
                proxyIp = ipField.getText();
                proxyAuth = authField.getText();
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
        this.ipField.textboxKeyTyped(c, i);
        this.authField.textboxKeyTyped(c, i);
        if (c == '\r')
        {
            this.actionPerformed((GuiButton) this.controlList.get(1));
        }

        ((GuiButton) this.controlList.get(1)).enabled = this.ipField.getText().isEmpty() || (this.ipField.getText().split(":").length == 2 && this.ipField.getText().length() > 8);
    }

    @Override
    protected void mouseClicked(int i, int j, int k) {
        super.mouseClicked(i, j, k);
        this.ipField.mouseClicked(i, j, k);
        this.authField.mouseClicked(i, j, k);
    }

    @Override
    public void drawScreen(int i, int j, float f) {
        drawDefaultBackground();
        drawCenteredString(fontRenderer, "Proxy", width / 2, 10, -1);

        this.ipField.drawTextBox();
        this.authField.drawTextBox();

        if (this.ipField.getText().isEmpty()) drawString(fontRenderer, "ip:port", this.width / 2 - 96, this.height / 2 - 26, 0x707070);
        if (this.authField.getText().isEmpty()) drawString(fontRenderer, "user:name (optional)", this.width / 2 - 96, this.height / 2 - 2, 0x707070);

        super.drawScreen(i, j, f);
    }
}
