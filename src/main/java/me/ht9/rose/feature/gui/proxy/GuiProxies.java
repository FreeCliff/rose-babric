package me.ht9.rose.feature.gui.proxy;

import me.ht9.rose.feature.GuiConfirm;
import me.ht9.rose.feature.registry.Registry;
import net.minecraft.src.*;

public class GuiProxies extends GuiScreen
{
    private static ProxyData currentProxy = Registry.proxies().get(0);

    private final GuiScreen guiScreen;
    private ProxySlot proxySlot;

    public GuiButton buttonCheck;
    public GuiButton buttonEdit;
    public GuiButton buttonDelete;

    public GuiProxies(GuiScreen guiScreen)
    {
        this.guiScreen = guiScreen;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initGui()
    { // check proxies, import proxies, add proxy, edit proxy, delete proxy
        this.controlList.add(new GuiButton(1, this.width / 2 + 4, this.height - 28, 150, 20, "Done"));
        this.controlList.add(new GuiButton(2, this.width / 2 + 4, this.height - 52, 70, 20, "Add"));

        GuiButton buttonImport = new GuiButton(3, this.width / 2 + 84, this.height - 52, 70, 20, "Import");
        this.controlList.add(buttonImport);
        buttonImport.enabled = false; // for now cause I'm too lazy to finish this

        this.controlList.add(this.buttonEdit = new GuiButton(4, this.width / 2 - 154, this.height - 52, 150, 20, "Edit"));
        this.controlList.add(this.buttonCheck = new GuiButton(5, this.width / 2 - 154, this.height - 28, 70, 20, "Check"));
        this.controlList.add(this.buttonDelete = new GuiButton(6, this.width / 2 - 74, this.height - 28, 70, 20, "Delete"));

        if (currentProxy.equals(Registry.proxies().get(0)))
        {
            this.buttonCheck.enabled = this.buttonEdit.enabled = this.buttonDelete.enabled = false;
        }

        this.proxySlot = new ProxySlot(this);
        this.proxySlot.registerScrollButtons(this.controlList, 7, 8);
    }

    @Override
    protected void actionPerformed(GuiButton guiButton)
    {
        if (guiButton.enabled)
        {
            switch (guiButton.id)
            {
                case 1 -> mc.displayGuiScreen(guiScreen);
                case 2 -> mc.displayGuiScreen(new GuiProxy(this, null));
                case 4 -> mc.displayGuiScreen(new GuiProxy(this, currentProxy));
                case 6 ->
                        mc.displayGuiScreen(new GuiConfirm(this, "Are you sure you want to delete this proxy?", "It will be lost forever!", "Delete", "Cancel", (result) ->
                        {
                            if (result)
                            {
                                Registry.proxies().remove(currentProxy);
                                currentProxy = Registry.proxies().get(0);
                            }
                            mc.displayGuiScreen(this);
                        }));
                default -> this.proxySlot.actionPerformed(guiButton);
            }
        }
    }

    @Override
    public void drawScreen(int i, int j, float f)
    {
        this.proxySlot.drawScreen(i, j, f);

        this.drawCenteredString(this.fontRenderer, "Proxies", this.width / 2, 16, 0xffffff);
        super.drawScreen(i, j, f);
    }

    public static ProxyData currentProxy()
    {
        return currentProxy;
    }

    class ProxySlot extends GuiSlot
    {
        private final GuiProxies gui;

        public ProxySlot(GuiProxies gui)
        {
            super(gui.mc, gui.width, gui.height, 32, gui.height - 64, 36);
            this.gui = gui;
        }

        @Override
        protected int getSize()
        {
            return Registry.proxies().size();
        }

        @Override
        protected void elementClicked(int i, boolean bl)
        {
            currentProxy = Registry.proxies().get(i);
            gui.buttonCheck.enabled = gui.buttonEdit.enabled = gui.buttonDelete.enabled = !currentProxy.equals(Registry.proxies().get(0));
        }

        @Override
        protected boolean isSelected(int i)
        {
            return Registry.proxies().get(i).equals(currentProxy);
        }

        @Override
        protected int getContentHeight()
        {
            return this.getSize() * 36;
        }

        @Override
        protected void drawBackground()
        {
            gui.drawDefaultBackground();
        }

        @Override
        protected void drawSlot(int i, int x, int y, int l, Tessellator tessellator)
        {
            ProxyData proxy = Registry.proxies().get(i);
            drawString(mc.fontRenderer, proxy.name(), x + 2, y + 1, 0xffffff);
            if (proxy.ip() != null) drawString(mc.fontRenderer, "IP: " + proxy.ip(), x + 2, y + 12, 0x808080);
            drawString(mc.fontRenderer, "Type: " + proxy.type(), x + 2, y + 12 + 10, 0x808080);
        }
    }
}
