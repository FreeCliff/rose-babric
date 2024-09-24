package me.ht9.rose.mixin.mixins;

import me.ht9.rose.Rose;
import me.ht9.rose.event.events.Render2dEvent;
import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = GuiIngame.class)
public abstract class MixinGuiIngame
{
    @Inject(
            method = "renderGameOverlay",
            at = @At(
                    value = "TAIL"
            )
    )
    public void renderText(float f, boolean flag, int i, int j, CallbackInfo ci)
    {
        Rose.bus().post(new Render2dEvent(f));
    }
}