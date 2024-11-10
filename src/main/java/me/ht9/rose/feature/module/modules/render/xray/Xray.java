package me.ht9.rose.feature.module.modules.render.xray;

import me.ht9.rose.event.bus.annotation.SubscribeEvent;
import me.ht9.rose.event.events.BlockRenderSideEvent;
import me.ht9.rose.feature.module.Module;
import me.ht9.rose.feature.module.annotation.Description;

import java.util.Arrays;
import java.util.List;

@Description("Only render ores")
public final class Xray extends Module {
    private static final Xray instance = new Xray();
    private static final List<Integer> ORES = Arrays.asList(7, 8, 9, 10, 11, 14, 15, 16, 20, 21, 22, 23, 29, 33, 41, 42, 46, 48, 49, 52, 54, 55, 56, 57, 58, 61, 62, 63, 64, 68, 71, 73, 74, 75, 76);

    @Override
    public void onEnable() {
        mc.theWorld.markBlocksDirty((int) mc.thePlayer.posX - 250, 0, (int) mc.thePlayer.posZ - 250, (int) mc.thePlayer.posX + 250, 130, (int) mc.thePlayer.posZ + 250);
    }

    @Override
    public void onDisable() {
        mc.theWorld.markBlocksDirty((int) mc.thePlayer.posX - 250, 0, (int) mc.thePlayer.posZ - 250, (int) mc.thePlayer.posX + 250, 130, (int) mc.thePlayer.posZ + 250);
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public void onBlockRender(BlockRenderSideEvent event) {
        if (!ORES.contains(event.block().blockID))
            event.setCancelled(true);
        else
            event.setForceRender(true);
    }

    public static Xray instance() {
        return instance;
    }
}
