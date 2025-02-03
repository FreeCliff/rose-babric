package me.ht9.rose.feature.module.modules.misc.portals;

import me.ht9.rose.feature.module.Module;
import me.ht9.rose.feature.module.annotation.Description;

@Description("Allows you to use GUIs normally while in a nether portal")
public final class Portals extends Module
{
    private static final Portals instance = new Portals();

    public static Portals instance()
    {
        return instance;
    }
}
