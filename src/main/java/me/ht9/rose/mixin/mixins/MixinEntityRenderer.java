package me.ht9.rose.mixin.mixins;

import me.ht9.rose.Rose;
import me.ht9.rose.event.events.RenderWorldEvent;
import me.ht9.rose.event.events.RenderWorldPassEvent;
import net.minecraft.src.EntityRenderer;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = EntityRenderer.class)
public class MixinEntityRenderer
{
    @Inject(
            method = "renderWorld",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/src/EntityRenderer;func_4135_b(FI)V"
            )
    )
    public void renderWorld(float partialTicks, long idk, CallbackInfo ci)
    {
        RenderWorldPassEvent event = new RenderWorldPassEvent(partialTicks);
        Rose.bus().post(event);
    }

    @Inject(
            method = "setupFog",
            at = @At(
                    value = "TAIL"
            )
    )
    public void setupFog(int startCoords, float partialTicks, CallbackInfo ci)
    {
        RenderWorldEvent event = new RenderWorldEvent(RenderWorldEvent.Type.FOG);
        Rose.bus().post(event);
        if (event.cancelled())
        {
            GL11.glFogi(2916, 9729);
        }
    }

    @Inject(
            method = "hurtCameraEffect",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    public void hurtCameraEffect(float partialTicks, CallbackInfo ci)
    {
        RenderWorldEvent event = new RenderWorldEvent(RenderWorldEvent.Type.HURTCAM);
        Rose.bus().post(event);
        if (event.cancelled())
        {
            ci.cancel();
        }
    }

    @Inject(
            method = "addRainParticles",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    public void addRainParticles(CallbackInfo ci)
    {
        RenderWorldEvent event = new RenderWorldEvent(RenderWorldEvent.Type.RAIN);
        Rose.bus().post(event);
        if (event.cancelled())
        {
            ci.cancel();
        }
    }

    @Inject(
            method = "renderRainSnow",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    public void renderRainSnow(float partialTicks, CallbackInfo ci)
    {
        RenderWorldEvent event = new RenderWorldEvent(RenderWorldEvent.Type.RAIN);
        Rose.bus().post(event);
        if (event.cancelled())
        {
            ci.cancel();
        }
    }

    @Inject(
            method = "setupViewBobbing",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    public void setupViewBobbing(float partialTicks, CallbackInfo ci)
    {
        RenderWorldEvent event = new RenderWorldEvent(RenderWorldEvent.Type.VIEWBOB);
        Rose.bus().post(event);
        if (event.cancelled())
        {
            ci.cancel();
        }
    }
}
