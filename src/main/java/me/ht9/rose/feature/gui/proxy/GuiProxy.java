package me.ht9.rose.feature.gui.proxy;

import me.ht9.rose.feature.gui.GuiTextField;
import me.ht9.rose.feature.registry.Registry;
import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiScreen;
import org.lwjgl.input.Keyboard;

import static java.net.Proxy.Type;

public final class GuiProxy extends GuiScreen
{
    private final ProxyData proxy;

    private final GuiScreen parentScreen;
    private Type type = Type.SOCKS;
    private GuiTextField nameField;
    private GuiTextField ipField;
    private GuiTextField authField;

    public GuiProxy(GuiScreen parentScreen, ProxyData proxy)
    {
        this.parentScreen = parentScreen;
        this.proxy = proxy;

        if (proxy != null) type = proxy.type();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initGui()
    {
        Keyboard.enableRepeatEvents(true);
        controlList.clear();
        controlList.add(new GuiButton(0, this.width / 2 - 100, this.height / 2 + 72 + 12, "Type: " + type));
        controlList.add(new GuiButton(1, this.width / 2 - 100, this.height / 2 + 96 + 12, "Set Proxy"));
        controlList.add(new GuiButton(2, this.width / 2 - 100, this.height / 2 + 120 + 12, "Cancel"));

        this.nameField = new GuiTextField(this, this.fontRenderer, this.width / 2 - 100, 60, 200, 20, proxy == null ? "" : proxy.name());
        nameField.isFocused = true;
        nameField.setMaxStringLength(30);

        this.ipField = new GuiTextField(this, this.fontRenderer, this.width / 2 - 100, 106, 200, 20, proxy == null ? "" : proxy.ip());
        ipField.setMaxStringLength(100);

        this.authField = new GuiTextField(this, this.fontRenderer, this.width / 2 - 100, 152, 200, 20, proxy == null ? "" : proxy.auth());
        authField.setMaxStringLength(100);

        ((GuiButton) this.controlList.get(1)).enabled = !this.nameField.getText().isEmpty() && (this.ipField.getText().split(":").length == 2 && this.ipField.getText().length() > 8);
    }

    @Override
    public void updateScreen()
    {
        this.nameField.updateCursorCounter();
        this.ipField.updateCursorCounter();
        this.authField.updateCursorCounter();
    }

    @Override
    public void onGuiClosed()
    {
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    protected void actionPerformed(GuiButton guiButton)
    {
        if (guiButton.enabled)
        {
            if (guiButton.id == 0)
            {
                if (type.equals(Type.HTTP))
                {
                    type = Type.SOCKS;
                } else
                {
                    type = Type.HTTP;
                }
                ((GuiButton) controlList.get(0)).displayString = "Type: " + type;
            } else if (guiButton.id == 1)
            {
                if (this.proxy != null)
                {
                    this.proxy.setName(nameField.getText());
                    this.proxy.setIp(ipField.getText());
                    this.proxy.setAuth(authField.getText());
                    this.proxy.setType(type);
                } else
                {
                    Registry.proxies().add(new ProxyData(nameField.getText(), ipField.getText(), authField.getText(), type));
                }

                mc.displayGuiScreen(parentScreen);
            } else if (guiButton.id == 2)
            {
                mc.displayGuiScreen(parentScreen);
            }
        }
    }

    @Override
    protected void keyTyped(char c, int i)
    {
        this.nameField.textboxKeyTyped(c, i);
        this.ipField.textboxKeyTyped(c, i);
        this.authField.textboxKeyTyped(c, i);
        if (c == '\r')
        {
            this.actionPerformed((GuiButton) this.controlList.get(1));
        }

        ((GuiButton) this.controlList.get(1)).enabled = !this.nameField.getText().isEmpty() && (this.ipField.getText().split(":").length == 2 && this.ipField.getText().length() > 8);
    }

    @Override
    protected void mouseClicked(int i, int j, int k)
    {
        super.mouseClicked(i, j, k);
        this.nameField.mouseClicked(i, j, k);
        this.ipField.mouseClicked(i, j, k);
        this.authField.mouseClicked(i, j, k);
    }

    @Override
    public void drawScreen(int i, int j, float f)
    {
        drawDefaultBackground();
        drawCenteredString(fontRenderer, (proxy == null ? "Add" : "Edit") + " Proxy", width / 2, 10, -1);

        drawString(fontRenderer, "Proxy name", this.width / 2 - 100, 47, 0xa0a0a0);
        this.nameField.drawTextBox();

        drawString(fontRenderer, "IP", this.width / 2 - 100, 93, 0xa0a0a0);
        this.ipField.drawTextBox();

        drawString(fontRenderer, "Auth (optional)", this.width / 2 - 100, 139, 0xa0a0a0);
        this.authField.drawTextBox();

        if (this.ipField.getText().isEmpty())
            drawString(fontRenderer, "ip:port", this.width / 2 - 96, 112, 0x707070);

        if (this.authField.getText().isEmpty())
            drawString(fontRenderer, "user:name", this.width / 2 - 96, 158, 0x707070);

        super.drawScreen(i, j, f);
    }
}
