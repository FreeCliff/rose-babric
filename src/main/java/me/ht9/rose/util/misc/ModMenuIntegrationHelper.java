package me.ht9.rose.util.misc;

import io.github.prospector.modmenu.ModMenu;

public final class ModMenuIntegrationHelper
{
    public static void addLegacyConfigScreenTask(String modId, Runnable task)
    {
        ModMenu.addLegacyConfigScreenTask(modId, task);
    }
}
