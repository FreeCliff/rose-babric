package me.ht9.rose.mixin.mixins;

import me.ht9.rose.Rose;
import me.ht9.rose.event.events.BlockRenderSideEvent;
import net.minecraft.src.Block;
import net.minecraft.src.BlockSnow;
import net.minecraft.src.IBlockAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = BlockSnow.class)
public class MixinBlockSnow
{
    @Inject(
            method = "shouldSideBeRendered",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    public void shouldSideBeRendered(IBlockAccess i, int j, int k, int l, int par5, CallbackInfoReturnable<Boolean> cir)
    {
        BlockRenderSideEvent event = new BlockRenderSideEvent((Block) (Object) this);
        Rose.bus().post(event);
        if (event.cancelled())
        {
            cir.setReturnValue(false);
            cir.cancel();
        }
        if (event.forceRender())
        {
            cir.setReturnValue(true);
            cir.cancel();
        }
    }
}
