package me.ht9.rose.mixin.mixins;

import me.ht9.rose.Rose;
import me.ht9.rose.event.events.RenderWorldEvent;
import net.minecraft.src.RenderGlobal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = RenderGlobal.class)
public class MixinRenderGlobal
{
    @Inject(
            method = "renderClouds",
            at = @At(
                    "HEAD"
            ),
            cancellable = true
    )
    private void renderClouds(float par1, CallbackInfo ci)
    {
        RenderWorldEvent event = new RenderWorldEvent(RenderWorldEvent.Type.CLOUD);
        Rose.bus().post(event);
        if (event.cancelled())
            ci.cancel();
    }
}
