package me.ht9.rose.feature.module.modules.misc.fastplace;

import me.ht9.rose.feature.module.Module;
import me.ht9.rose.feature.module.annotation.Description;

@Description("Places fast.")
public final class FastPlace extends Module
{
    private static final FastPlace instance = new FastPlace();

    public static FastPlace instance()
    {
        return instance;
    }
}
