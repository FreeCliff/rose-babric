package me.ht9.rose.feature.module.modules.client.hudeditor;

import me.ht9.rose.feature.module.Module;
import me.ht9.rose.feature.module.annotation.Description;
import me.ht9.rose.feature.module.modules.client.clickgui.ClickGUI;

@Description("Manage the clients heads-up display.")
public final class HudEditor extends Module
{
    private static final HudEditor instance = new HudEditor();

    @Override
    public void onEnable()
    {
        ClickGUI.instance().disable();
        this.toggle();
    }

    public static HudEditor instance()
    {
        return instance;
    }
}
