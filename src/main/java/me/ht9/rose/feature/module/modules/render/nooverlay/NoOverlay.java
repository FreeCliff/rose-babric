package me.ht9.rose.feature.module.modules.render.nooverlay;

import me.ht9.rose.event.bus.annotation.SubscribeEvent;
import me.ht9.rose.event.events.RenderOverlayEvent;
import me.ht9.rose.feature.module.Module;
import me.ht9.rose.feature.module.annotation.Description;
import me.ht9.rose.feature.module.setting.Setting;

@Description("Block overlays in first-person.")
public class NoOverlay extends Module
{
    private static final NoOverlay instance = new NoOverlay();

    private final Setting<Boolean> blocks = new Setting<>("Blocks", true);
    private final Setting<Boolean> hand = new Setting<>("Hand", false);
    private final Setting<Boolean> fire = new Setting<>("Fire", true);
    private final Setting<Boolean> vignette = new Setting<>("Vignette", false);

    @SubscribeEvent
    public void onRenderOverlay(RenderOverlayEvent event)
    {
        switch (event.overlay())
        {
            case BLOCKS ->
            {
                if (this.blocks.value())
                {
                    event.setCancelled(true);
                }
            }
            case HAND ->
            {
                if (this.hand.value())
                {
                    event.setCancelled(true);
                }
            }
            case FIRE ->
            {
                if (this.fire.value())
                {
                    event.setCancelled(true);
                }
            }
            case VIGNETTE ->
            {
                if (this.vignette.value())
                {
                    event.setCancelled(true);
                }
            }
            default -> throw new IllegalStateException("Unexpected value: " + event.overlay());
        }
    }

    public static NoOverlay instance()
    {
        return instance;
    }
}
