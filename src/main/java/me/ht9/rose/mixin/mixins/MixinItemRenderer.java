package me.ht9.rose.mixin.mixins;

import me.ht9.rose.Rose;
import me.ht9.rose.event.events.RenderOverlayEvent;
import me.ht9.rose.event.events.ItemRenderEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.ItemRenderer;
import net.minecraft.src.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ItemRenderer.class)
public class MixinItemRenderer
{
    @Inject(
            method = "renderInsideOfBlock",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    private void renderInsideOfBlock(float f, int i, CallbackInfo ci)
    {
        RenderOverlayEvent event = new RenderOverlayEvent(RenderOverlayEvent.Overlay.BLOCKS);
        Rose.bus().post(event);
        if (event.cancelled())
            ci.cancel();
    }

    @Inject(
            method = "renderFireInFirstPerson",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    private void renderFireInFirstPerson(final float par1, final CallbackInfo ci)
    {
        RenderOverlayEvent event = new RenderOverlayEvent(RenderOverlayEvent.Overlay.FIRE);
        Rose.bus().post(event);
        if (event.cancelled())
            ci.cancel();
    }

    @Inject(
            method = "renderItemInFirstPerson",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    private void renderHandInFirstPerson(final float par1, final CallbackInfo ci)
    {
        RenderOverlayEvent event = new RenderOverlayEvent(RenderOverlayEvent.Overlay.HAND);
        Rose.bus().post(event);
        if (event.cancelled())
            ci.cancel();
    }

    @Inject(
            method = "renderItem",
            at = @At(
                    value = "HEAD"
            )
    )
    private void renderItem(EntityLiving entity, ItemStack itemStack, CallbackInfo ci)
    {
        Rose.bus().post(new ItemRenderEvent(entity, itemStack));
    }
}