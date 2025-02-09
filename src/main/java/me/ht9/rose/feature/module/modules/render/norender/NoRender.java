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
    private final Setting<Boolean> portal = new Setting<>("Portal", false);
    private final Setting<Boolean> entities = new Setting<>("Entities", false);
    private final Setting<Boolean> particles = new Setting<>("Particles", false);

    @Override
    public void onRender2d(float partialTicks)
    {
        if (portal.value() && mc.thePlayer != null)
        {
            mc.thePlayer.field_504 = 0.0f; // shitty mappings </3
            mc.thePlayer.prevTimeInPortal = 0.0f;
        }
    }

    @SuppressWarnings("unused")
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
            case ENTITIES ->
            {
                if (this.entities.value())
                {
                    event.setCancelled(true);
                }
            }
            case PARTICLES ->
            {
                if (this.particles.value())
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
