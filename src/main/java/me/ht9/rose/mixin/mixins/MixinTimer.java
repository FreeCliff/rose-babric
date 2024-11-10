package me.ht9.rose.mixin.mixins;

import me.ht9.rose.Rose;
import me.ht9.rose.event.events.TimerUpdateEvent;
import net.minecraft.src.Timer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Timer.class)
public class MixinTimer {
    @Shadow public float timerSpeed;

    @Inject(method = "updateTimer", at = @At("HEAD"), cancellable = true)
    public void onTimerUpdate(CallbackInfo ci) {
        TimerUpdateEvent event = new TimerUpdateEvent();
        Rose.bus().post(event);
        if (event.cancelled()) ci.cancel();
        timerSpeed = event.timerSpeed();
    }
}
