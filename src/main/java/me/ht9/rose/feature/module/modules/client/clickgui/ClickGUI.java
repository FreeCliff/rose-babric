package me.ht9.rose.feature.module.modules.client.clickgui;

import me.ht9.rose.feature.gui.RoseGui;
import me.ht9.rose.feature.module.annotation.Aliases;
import me.ht9.rose.feature.module.annotation.Description;
import me.ht9.rose.feature.module.keybinding.Bind;
import me.ht9.rose.feature.module.setting.Setting;
import me.ht9.rose.feature.module.Module;
import org.lwjgl.input.Keyboard;

@Aliases({ "GUI" })
@Description("The clients graphical user-interface.")
public final class ClickGUI extends Module
{
    private static final ClickGUI instance = new ClickGUI();

    public final Setting<Integer> red = new Setting<>("Red", 0, 105, 255);
    public final Setting<Integer> green = new Setting<>("Green", 0, 130, 255);
    public final Setting<Integer> blue = new Setting<>("Blue", 0, 255, 255);

    private ClickGUI()
    {
        this.toggleBind().value().setKey(Keyboard.KEY_RSHIFT, Bind.BindType.KEYBOARD);
    }

    @Override
    public void onEnable()
    {
        RoseGui.instance().openGui();
    }

    @Override
    public void onDisable()
    {
        if (mc.currentScreen instanceof RoseGui)
        {
            mc.displayGuiScreen(null);
        }
    }

    public static ClickGUI instance()
    {
        return instance;
    }
}
