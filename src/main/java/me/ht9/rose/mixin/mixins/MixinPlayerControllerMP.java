package me.ht9.rose.mixin.mixins;

import me.ht9.rose.Rose;
import me.ht9.rose.event.events.PlayerDamageBlockEvent;
import me.ht9.rose.event.events.SyncCurrentItemEvent;
import me.ht9.rose.feature.module.modules.exploit.infdurability.InfDurability;
import me.ht9.rose.mixin.accessors.IBlock;
import net.minecraft.src.Block;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.PlayerControllerMP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = PlayerControllerMP.class)
public abstract class MixinPlayerControllerMP
{
    @Shadow
    private int blockHitDelay;

    @Shadow protected abstract void syncCurrentPlayItem();

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

    @Redirect(method = "sendBlockRemoving", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/PlayerControllerMP;syncCurrentPlayItem()V"))
    public void syncCurrentPlayItem(PlayerControllerMP instance)
    {
        SyncCurrentItemEvent event = new SyncCurrentItemEvent(SyncCurrentItemEvent.Type.BREAK);
        Rose.bus().post(event);
        if (event.cancelled())
            return;
        syncCurrentPlayItem();
    }

    @Redirect(method = "sendBlockRemoving", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/Block;blockStrength(Lnet/minecraft/src/EntityPlayer;)F"))
    public float blockStrength(Block block, EntityPlayer player)
    {
        if (!InfDurability.instance().enabled())
            return block.blockStrength(player);

        if (((IBlock)block).getBlockHardness() < 0.0f)
            return 0.0f;

        boolean canHarvestBlock = block.blockMaterial.getIsHarvestable() || (player.inventory.mainInventory[9] != null && player.inventory.mainInventory[9].canHarvestBlock(block));

        if (canHarvestBlock)
        {
            float strVsBlock = 1.0f;

            if (player.inventory.mainInventory[9] != null)
                strVsBlock *= player.inventory.mainInventory[9].getStrVsBlock(block);

            if (player.isInWater())
                strVsBlock /= 5.0f;

            if (!player.onGround)
                strVsBlock /= 5.0f;

            return strVsBlock / ((IBlock)block).getBlockHardness() / 30.0f;
        }
        else
        {
            return 1.0f / ((IBlock)block).getBlockHardness() / 100.0f;
        }
    }
}
