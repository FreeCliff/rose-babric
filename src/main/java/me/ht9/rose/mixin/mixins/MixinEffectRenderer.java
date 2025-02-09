package me.ht9.rose.mixin.mixins;

import me.ht9.rose.Rose;
import me.ht9.rose.event.events.RenderWorldEvent;
import net.minecraft.src.EffectRenderer;
import net.minecraft.src.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EffectRenderer.class)
public class MixinEffectRenderer
{
    @Inject(method = "renderParticles", at = @At("HEAD"), cancellable = true)
    public void renderParticles$Head(Entity renderViewEntity, float partialTicks, CallbackInfo ci)
    {
        RenderWorldEvent event = new RenderWorldEvent(RenderWorldEvent.Type.PARTICLES);
        Rose.bus().post(event);
        if (event.cancelled())
            ci.cancel();
    }
}
