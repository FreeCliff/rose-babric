package me.ht9.rose.mixin.mixins;

import me.ht9.rose.feature.module.modules.render.norender.NoRender;
import net.minecraft.src.TileEntity;
import net.minecraft.src.TileEntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TileEntityRenderer.class)
public class MixinTileEntityRenderer
{
    @Inject(method = "renderTileEntity", at = @At("HEAD"), cancellable = true)
    private void renderTileEntity(TileEntity f, float par2, CallbackInfo ci)
    {
        if (NoRender.instance().tileEntities.value())
            ci.cancel();
    }
}
