package me.ht9.rose.feature.module.modules.movement.jesus;

import me.ht9.rose.feature.module.Module;
import me.ht9.rose.feature.module.annotation.Description;
import me.ht9.rose.feature.module.setting.Setting;

@Description("Lets you walk on water")
public final class Jesus extends Module
{
    private static final Jesus instance = new Jesus();

    public final Setting<Mode> mode = new Setting<>("Mode", Mode.SemiSolid);

    public static Jesus instance()
    {
        return instance;
    }

    public enum Mode
    {
        Solid,
        SemiSolid,
        Bob
    }
}
