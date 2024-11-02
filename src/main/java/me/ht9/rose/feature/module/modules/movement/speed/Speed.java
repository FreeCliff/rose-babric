package me.ht9.rose.feature.module.modules.movement.speed;

import me.ht9.rose.event.bus.annotation.SubscribeEvent;
import me.ht9.rose.event.events.PacketEvent;
import me.ht9.rose.event.events.PosRotUpdateEvent;
import me.ht9.rose.event.events.PushByEvent;
import me.ht9.rose.feature.module.Module;
import me.ht9.rose.feature.module.annotation.Description;
import me.ht9.rose.feature.module.setting.Setting;
import me.ht9.rose.mixin.accessors.IMinecraft;
import me.ht9.rose.util.module.Movement;
import me.ht9.rose.util.module.Timer;
import net.minecraft.src.Explosion;
import net.minecraft.src.Packet19EntityAction;
import net.minecraft.src.Packet28EntityVelocity;
import net.minecraft.src.Packet60Explosion;

@Description(".")
public class Speed extends Module
{
    private static final Speed instance = new Speed();

    public final Setting<Type> type = new Setting<>("Type", Type.NoCheat);
    public final Setting<Double> speed = new Setting<>("Speed", 0.0, 0.5, 1.0, 1);

    private final Timer timer = new Timer();

    public void onEnable()
    {
        this.timer.reset();
    }

    @SubscribeEvent
    public void onUpdate(PosRotUpdateEvent event)
    {
        if(this.type.value() == Type.NoCheat)
        {
            if (this.timer.hasReached(450))
            {
                mc.getSendQueue().addToSendQueue(new Packet19EntityAction(mc.thePlayer, 3));
                this.timer.reset();
            }
            Movement.setSpeed(
                    mc.thePlayer.movementInput.moveForward != 0 || mc.thePlayer.movementInput.moveStrafe != 0
                            ? speed.value()
                            : 0
            );
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