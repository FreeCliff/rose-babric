package me.ht9.rose.feature.module.modules.movement.faststop;

import me.ht9.rose.event.bus.annotation.SubscribeEvent;
import me.ht9.rose.event.events.PosRotUpdateEvent;
import me.ht9.rose.feature.module.Module;
import me.ht9.rose.feature.module.annotation.Description;
import net.minecraft.src.MovementInput;

@Description("Instantly stops all movement")
public final class FastStop extends Module
{
    private static final FastStop instance = new FastStop();

    @SubscribeEvent
    public void onUpdate(PosRotUpdateEvent event)
    {
        MovementInput input = mc.thePlayer.movementInput;

        if (input.moveForward == 0.0f && input.moveStrafe == 0.0f)
        {
            mc.thePlayer.motionX = 0.0;
            mc.thePlayer.motionZ = 0.0;
        }
    }

    public static FastStop instance()
    {
        return instance;
    }
}
