package me.ht9.rose.feature.module.modules.render.fullbright;

import me.ht9.rose.event.bus.annotation.SubscribeEvent;
import me.ht9.rose.event.events.WorldChangeEvent;
import me.ht9.rose.feature.module.Module;
import me.ht9.rose.feature.module.annotation.Description;

import java.util.Arrays;

@Description("Make the world bright!")
public final class FullBright extends Module {
    private static final FullBright instance = new FullBright();
    private float[] lightBrightnessTable = new float[16];

    @Override
    public void onEnable() {
        this.lightBrightnessTable = Arrays.copyOf(mc.theWorld.worldProvider.lightBrightnessTable, 16);
        Arrays.fill(mc.theWorld.worldProvider.lightBrightnessTable, 1.0f);
        mc.theWorld.markBlocksDirty((int) mc.thePlayer.posX - 250, 0, (int) mc.thePlayer.posZ - 250, (int) mc.thePlayer.posX + 250, 130, (int) mc.thePlayer.posZ + 250);
    }

    @Override
    public void onDisable() {
        mc.theWorld.worldProvider.lightBrightnessTable = Arrays.copyOf(this.lightBrightnessTable, 16);
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
