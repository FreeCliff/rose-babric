package me.ht9.rose.feature.module.modules.render.fullbright;

import me.ht9.rose.event.bus.annotation.SubscribeEvent;
import me.ht9.rose.event.events.WorldChangeEvent;
import me.ht9.rose.feature.module.Module;
import me.ht9.rose.feature.module.annotation.Description;

import java.util.Arrays;

@Description("Make the world bright!")
public final class FullBright extends Module {
    private static final FullBright instance = new FullBright();

    @Override
    public void onEnable() {
        Arrays.fill(mc.theWorld.worldProvider.lightBrightnessTable, 1.0f);
        mc.renderGlobal.loadRenderers();
    }

    @Override
    public void onDisable() {
        float var1 = 0.05F;

        for(int var2 = 0; var2 <= 15; ++var2) {
            float var3 = 1.0F - (float)var2 / 15.0F;
            mc.theWorld.worldProvider.lightBrightnessTable[var2] = (1.0F - var3) / (var3 * 3.0F + 1.0F) * (1.0F - var1) + var1;
        }

        mc.renderGlobal.loadRenderers();
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public void onWorldChange(WorldChangeEvent event) {
        this.onEnable();
    }

    public static FullBright instance() {
        return instance;
    }
}
