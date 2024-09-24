package me.ht9.rose.mixin.mixins;

import me.ht9.rose.Rose;
import me.ht9.rose.event.events.RenderOverlayEvent;
import net.minecraft.src.ItemRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ItemRenderer.class)
public class MixinItemRenderer
{
    @Inject(method = "renderOverlays", at = @At(value = "HEAD"), cancellable = true)
    public void renderOverlays(float partialTicks, CallbackInfo ci)
    {
        RenderOverlayEvent event = new RenderOverlayEvent(RenderOverlayEvent.Overlay.ALL);
        Rose.bus().post(event);
        if (event.cancelled())
        {
            ci.cancel();
        }
    }

    @Inject(
            method = "renderInsideOfBlock",
            at = @At(
                    "HEAD"
            ),
            cancellable = true
    )
    private void renderInsideOfBlock(float f, int i, CallbackInfo ci)
    {
        RenderOverlayEvent event = new RenderOverlayEvent(RenderOverlayEvent.Overlay.BLOCKS);
        Rose.bus().post(event);
        if (event.cancelled())
        {
            ci.cancel();
        }
    }

    @Inject(
            method = "renderFireInFirstPerson",
            at = @At(
                    "HEAD"
            ),
            cancellable = true
    )
    private void renderFireInFirstPerson(final float par1, final CallbackInfo ci)
    {
        RenderOverlayEvent event = new RenderOverlayEvent(RenderOverlayEvent.Overlay.FIRE);
        Rose.bus().post(event);
        if (event.cancelled())
        {
            ci.cancel();
        }
    }

    @Inject(
            method = "renderItemInFirstPerson",
            at = @At(
                    "HEAD"
            ),
            cancellable = true
    )
    private void renderHandInFirstPerson(final float par1, final CallbackInfo ci)
    {
        RenderOverlayEvent event = new RenderOverlayEvent(RenderOverlayEvent.Overlay.HAND);
        Rose.bus().post(event);
        if (event.cancelled())
        {
            ci.cancel();
        }
    }
}