package me.ht9.rose.feature.module.modules.render.norender;

import me.ht9.rose.event.bus.annotation.SubscribeEvent;
import me.ht9.rose.event.events.RenderWorldEvent;
import me.ht9.rose.feature.module.Module;
import me.ht9.rose.feature.module.annotation.Description;
import me.ht9.rose.feature.module.setting.Setting;

@Description("Prevent world and ui elements from rendering.")
public class NoRender extends Module
{
    private static final NoRender instance = new NoRender();

    private final Setting<Boolean> cloud = new Setting<>("Clouds", false);
    private final Setting<Boolean> fog = new Setting<>("Fog", true);
    private final Setting<Boolean> hurtcam = new Setting<>("Hurtcam", false);
    private final Setting<Boolean> rain = new Setting<>("Rain", false);
    private final Setting<Boolean> viewbob = new Setting<>("Viewbob", false);

    @SubscribeEvent
    public void onRenderWorld(RenderWorldEvent event)
    {
        switch (event.type())
        {
            case CLOUD ->
            {
                if (this.cloud.value())
                {
                    event.setCancelled(true);
                }
            }
            case FOG ->
            {
                if (this.fog.value())
                {
                    event.setCancelled(true);
                }
            }
            case HURTCAM ->
            {
                if (this.hurtcam.value())
                {
                    event.setCancelled(true);
                }
            }
            case RAIN ->
            {
                if (this.rain.value())
                {
                    event.setCancelled(true);
                }
            }
            case VIEWBOB ->
            {
                if (this.viewbob.value())
                {
                    event.setCancelled(true);
                }
            }
            default -> throw new IllegalStateException("Unexpected value: " + event.type());
        }
    }

    public static NoRender instance()
    {
        return instance;
    }
}
