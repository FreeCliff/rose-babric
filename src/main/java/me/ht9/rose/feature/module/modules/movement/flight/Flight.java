package me.ht9.rose.feature.module.modules.movement.flight;

import me.ht9.rose.event.bus.annotation.SubscribeEvent;
import me.ht9.rose.event.events.PosRotUpdateEvent;
import me.ht9.rose.feature.module.Module;
import me.ht9.rose.feature.module.annotation.Description;
import me.ht9.rose.feature.module.setting.Setting;
import me.ht9.rose.util.module.Movement;
import org.lwjgl.input.Keyboard;

@Description("fly")
public final class Flight extends Module {
    private static final Flight instance = new Flight();

    private final Setting<Float> speed = new Setting<>("Speed", 0.2f, 2.5f, 15.0f, 1);

    private Flight()
    {
        setArrayListInfo(() -> speed.value().toString());
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public void onUpdate(PosRotUpdateEvent event) {
        if (mc.thePlayer == null || mc.theWorld == null) return;
        mc.thePlayer.motionY = 0.0f;

        if (Keyboard.isKeyDown(mc.gameSettings.keyBindJump.keyCode) && mc.currentScreen == null) {
            mc.thePlayer.motionY = speed.value();
        }
        if (Keyboard.isKeyDown(mc.gameSettings.keyBindSneak.keyCode) && mc.currentScreen == null) {
            mc.thePlayer.motionY = -speed.value();
        }

        Movement.setSpeed(
                mc.thePlayer.movementInput.moveForward != 0 || mc.thePlayer.movementInput.moveStrafe != 0
                        ? speed.value()
                        : 0
        );
    }

    public static Flight instance() {
        return instance;
    }
}
