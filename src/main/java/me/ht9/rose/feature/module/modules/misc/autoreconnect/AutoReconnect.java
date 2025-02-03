package me.ht9.rose.feature.module.modules.misc.autoreconnect;

import me.ht9.rose.feature.module.Module;
import me.ht9.rose.feature.module.annotation.Description;
import me.ht9.rose.feature.module.setting.Setting;

@Description("Automatically reconnects to servers")
public final class AutoReconnect extends Module
{
    private static final AutoReconnect instance = new AutoReconnect();

    public final Setting<Double> delay = new Setting<>("Delay", 1.0, 7.5, 15.0, 1);

    private AutoReconnect()
    {
        setArrayListInfo(() -> delay.value().toString());
    }

    public static AutoReconnect instance()
    {
        return instance;
    }
}
