package me.ht9.rose.feature.module.modules.movement.speed;

import me.ht9.rose.event.bus.annotation.SubscribeEvent;
import me.ht9.rose.event.events.TickEvent;
import me.ht9.rose.feature.module.Module;
import me.ht9.rose.feature.module.annotation.Description;
import me.ht9.rose.feature.module.setting.Setting;
import me.ht9.rose.util.module.Movement;
import me.ht9.rose.util.module.Timer;
import net.minecraft.src.Packet19EntityAction;

@SuppressWarnings("unused")
@Description("Go fast")
public class Speed extends Module
{
    private static final Speed instance = new Speed();

    public final Setting<Type> type = new Setting<>("Type", Type.NoCheat);
    public final Setting<Double> speed = new Setting<>("Speed", 1.0, 5.0, 20.0, 1);

    private final Timer timer = new Timer();

    @Override
    public void onEnable()
    {
        this.timer.reset();
    }

    @Override
    public void onDisable() {
        if (this.type.value() == Type.NoCheat)
            mc.getSendQueue().addToSendQueue(new Packet19EntityAction(mc.thePlayer, 3));
    }

    @SubscribeEvent
    public void onTick(TickEvent event)
    {
        if(mc.thePlayer == null || mc.theWorld == null) return;
        if (this.type.value() == Type.Vanilla)
        {
            Movement.setSpeed(
                    mc.thePlayer.movementInput.moveForward != 0 || mc.thePlayer.movementInput.moveStrafe != 0
                            ? speed.value()
                            : 0
            );
        }
        else if (this.type.value() == Type.NoCheat)
        {
            if (!this.timer.hasReached(450)) return;
            mc.getSendQueue().addToSendQueue(new Packet19EntityAction(mc.thePlayer, 3));
            this.timer.reset();
        }
    }

    public static Speed instance()
    {
        return instance;
    }

    public enum Type
    {
        Vanilla,
        NoCheat
    }
}