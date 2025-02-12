package me.ht9.rose.feature.module.modules.movement.yaw;

import me.ht9.rose.event.bus.annotation.SubscribeEvent;
import me.ht9.rose.event.events.PosRotUpdateEvent;
import me.ht9.rose.feature.module.Module;
import me.ht9.rose.feature.module.annotation.Description;

@Description("Rotates your yaw to be perfect")
public final class Yaw extends Module {
    private static final Yaw instance = new Yaw();

    private float yaw = 0.0f;

    private Yaw()
    {
        setArrayListInfo(() -> String.valueOf(this.yaw));
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public void onUpdate(PosRotUpdateEvent event) {
        this.yaw = wrapAngleTo180(Math.round(mc.thePlayer.rotationYaw / 45.0f) * 45.0f);
        event.setYaw(this.yaw);
        event.setClientRotation(true);
    }

    private static float wrapAngleTo180(float f)
    {
        f %= 360.0f;

        if (f >= 180.0f) f -= 360.0f;
        if (f < -180.0f) f += 360.0f;

        return f;
    }

    public static Yaw instance() {
        return instance;
    }
}
