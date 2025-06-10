package me.ht9.rose.feature.module.modules.movement.speed;

import me.ht9.rose.event.bus.annotation.SubscribeEvent;
import me.ht9.rose.event.events.TickEvent;
import me.ht9.rose.feature.module.Module;
import me.ht9.rose.feature.module.annotation.Description;
import me.ht9.rose.feature.module.setting.Setting;
import me.ht9.rose.util.module.Movement;
import me.ht9.rose.util.module.Timer;
import net.minecraft.src.Packet;
import net.minecraft.src.Packet11PlayerPosition;
import net.minecraft.src.Packet19EntityAction;

import java.util.function.Supplier;

@SuppressWarnings("unused")
@Description("Go fast")
public final class Speed extends Module
{
    private static final Speed instance = new Speed();

    public final Setting<Type> type = new Setting<>("Type", Type.NoCheat);
    public final Setting<Double> speedd = new Setting<>("SpeedD", 1.0, 5.0, 20.0, 1, () -> type.value() == Type.Vanilla);
    public final Setting<Integer> speedi = new Setting<>("Speed", 1, 5, 20, () -> type.value() == Type.NoCheat);

    public final Setting<Bypass> bypass = new Setting<>("Bypass", Bypass.BedExit, () -> type.value().equals(Type.NoCheat));
    public final Setting<Double> delay = new Setting<>("Delay", 0.01, 0.45, 1.0, 3, () -> type.value().equals(Type.NoCheat));

    private final Timer timer = new Timer();

    private Speed()
    {
        setArrayListInfo(() -> type.value().toString());
    }

    @Override
    public void onEnable()
    {
        this.timer.reset();
    }

    @Override
    public void onDisable() {
        if (this.type.value() == Type.NoCheat)
            mc.getSendQueue().addToSendQueue(bypass.value().get());
    }

    @SubscribeEvent
    public void onTick(TickEvent event)
    {
        if(mc.thePlayer == null || mc.theWorld == null) return;
        if (this.type.value() == Type.NoCheat)
        {
            if (!this.timer.hasReached(delay.value(), true)) return;
            mc.getSendQueue().addToSendQueue(bypass.value().get());
        }
        else
        {
            Movement.setSpeed(
                    (this.type.value() == Type.Vanilla)
                    ? (mc.thePlayer.movementInput.moveForward != 0 || mc.thePlayer.movementInput.moveStrafe != 0
                            ? speedd.value() * .12
                            : 0)
                    : Math.sqrt(mc.thePlayer.motionX * mc.thePlayer.motionX + mc.thePlayer.motionZ * mc.thePlayer.motionZ)
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
        Strafe,
        NoCheat
    }

    public enum Bypass
    {
        BedExit(() -> new Packet19EntityAction(mc.thePlayer, 3)),
        Movement(() -> new Packet11PlayerPosition(mc.thePlayer.posX, mc.thePlayer.boundingBox.minY - 1.0, mc.thePlayer.posY - 1.0, mc.thePlayer.posZ, true));

        private final Supplier<? extends Packet> packetSupplier;

        Bypass(Supplier<? extends Packet> packetSupplier)
        {
            this.packetSupplier = packetSupplier;
        }

        public Packet get()
        {
            return packetSupplier.get();
        }
    }
}