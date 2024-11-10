package me.ht9.rose.feature.module.modules.movement.yaw;

import me.ht9.rose.event.bus.annotation.SubscribeEvent;
import me.ht9.rose.event.events.Render2dEvent;
import me.ht9.rose.feature.module.Module;
import me.ht9.rose.feature.module.annotation.Description;
import me.ht9.rose.feature.module.setting.Setting;

@Description("set your yaw")
public final class Yaw extends Module {
    private static final Yaw instance = new Yaw();

    public final Setting<Integer> direction = new Setting<>("Direction", 0, 0, 7);

    @SuppressWarnings("unused")
    @SubscribeEvent
    public void onUpdate(Render2dEvent event) { // doing this with PosRotUpdateEvent doesn't work :pray:
        if (mc.thePlayer == null) return;
        mc.thePlayer.rotationYaw = 45 * direction.value();
    }

    public static Yaw instance() {
        return instance;
    }
}
