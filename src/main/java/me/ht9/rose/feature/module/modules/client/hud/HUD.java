package me.ht9.rose.feature.module.modules.client.hud;

import me.ht9.rose.feature.module.Module;
import me.ht9.rose.feature.module.annotation.Description;

@Description("The clients heads-up display.")
public final class HUD extends Module
{
    private static final HUD instance = new HUD();

    @Override
    public void onRender2d(float partialTicks)
    {

    }

    public static HUD instance()
    {
        return instance;
    }
}
