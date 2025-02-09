package me.ht9.rose.feature.module.modules.render.cameratweaks;

import me.ht9.rose.Rose;
import me.ht9.rose.event.bus.annotation.SubscribeEvent;
import me.ht9.rose.event.events.InputEvent;
import me.ht9.rose.feature.module.Module;
import me.ht9.rose.feature.module.annotation.Description;
import me.ht9.rose.feature.module.setting.Setting;
import me.ht9.rose.mixin.accessors.EntityRendererAccessor;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

@Description("Allows modification of the third person camera")
public final class CameraTweaks extends Module
{
    private static final CameraTweaks instance = new CameraTweaks();

    public final Setting<Boolean> clip = new Setting<>("Clip", true);
    private final Setting<Float> cameraDistance = new Setting<>("CameraDistance", 0.0f, 4.0f, 15.0f, 1)
            .withOnChange(val ->
            {
                if (this.enabled()) ((EntityRendererAccessor) mc.entityRenderer).setCameraDistance(val);
            });

    public final Setting<Boolean> scrolling = new Setting<>("Scrolling", true);
    private final Setting<Float> scrollingSensitivity = new Setting<>("Sensitivity", 0.01f, 1.0f, 5.0f, 2, this.scrolling::value);

    @Override
    public void onEnable()
    {
        ((EntityRendererAccessor) mc.entityRenderer).setCameraDistance(this.cameraDistance.value());
    }

    @Override
    public void onDisable()
    {
        ((EntityRendererAccessor) mc.entityRenderer).setCameraDistance(4.0f);
    }

    @SubscribeEvent
    public void onKeyboard(InputEvent.KeyInputEvent event)
    {
        if (Keyboard.getEventKey() == Keyboard.KEY_F5 && Keyboard.getEventKeyState())
        {
            this.onEnable();
        }
    }

    @SubscribeEvent
    public void onMouse(InputEvent.MouseInputEvent event)
    {
        if (mc.currentScreen != null || !mc.gameSettings.thirdPersonView || !this.scrolling.value()) return;

        int value = Mouse.getEventDWheel();
        if (value != 0)
        {
            Rose.logger().info("value: {}", value);
            float dist = ((EntityRendererAccessor) mc.entityRenderer).cameraDistance();
            dist -= value * 0.05f * scrollingSensitivity.value();
            dist = Math.max(dist, 0.0f);
            ((EntityRendererAccessor) mc.entityRenderer).setCameraDistance(dist);
        }
    }

    public static CameraTweaks instance()
    {
        return instance;
    }
}
