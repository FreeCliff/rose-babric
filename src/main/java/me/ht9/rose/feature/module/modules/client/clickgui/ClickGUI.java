package me.ht9.rose.feature.module.modules.client.clickgui;

import me.ht9.rose.feature.gui.clickgui.RoseGui;
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

    public final Setting<Boolean> descriptions = new Setting<>("Descriptions", true)
            .withDescription("Whether or not to show this text when hovering things.");
    public final Setting<Boolean> customFont = new Setting<>("CustomFont", true)
            .withDescription("Whether or not to use a custom font instead of the built-in one.");
    public final Setting<Boolean> lowerCase = new Setting<>("LowerCase", false)
            .withDescription("Whether or not to make every rendered string lowercase.");

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
