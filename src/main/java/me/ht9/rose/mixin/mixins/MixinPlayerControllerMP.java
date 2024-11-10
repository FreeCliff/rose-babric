package me.ht9.rose.mixin.mixins;

import me.ht9.rose.Rose;
import me.ht9.rose.event.events.PlayerDamageBlockEvent;
import net.minecraft.src.PlayerControllerMP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = PlayerControllerMP.class)
public class MixinPlayerControllerMP
{
    @Shadow
    private int blockHitDelay;

    @Inject(
            method = "sendBlockRemoving",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    public void onPlayerDamageBlock(int x, int y, int z, int facing, CallbackInfo ci)
    {
        if (this.blockHitDelay <= 0)
        {
            PlayerDamageBlockEvent event = new PlayerDamageBlockEvent(x, y, z, facing);
            Rose.bus().post(event);
            if (event.cancelled())
            {
                ci.cancel();
            }
        }
    }
}
