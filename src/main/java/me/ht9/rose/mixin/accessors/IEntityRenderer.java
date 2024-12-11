package me.ht9.rose.mixin.accessors;

import net.minecraft.src.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(EntityRenderer.class)
public interface IEntityRenderer
{
    @Invoker("func_4135_b") void invokeRenderHand(float partialTicks, int pass);
    @Invoker("setupCameraTransform") void invokeSetupCameraTransform(float partialTicks, int pass);
}
