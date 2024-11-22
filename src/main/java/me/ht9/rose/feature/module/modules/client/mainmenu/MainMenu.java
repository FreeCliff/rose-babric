package me.ht9.rose.feature.module.modules.client.mainmenu;

import me.ht9.rose.feature.module.Module;
import me.ht9.rose.feature.module.annotation.Description;

@Description("Allows you to open the ClickGUI in the main menu.")
public class MainMenu extends Module
{
    private static final MainMenu instance = new MainMenu();

    public static MainMenu instance()
    {
        return instance;
    }
}