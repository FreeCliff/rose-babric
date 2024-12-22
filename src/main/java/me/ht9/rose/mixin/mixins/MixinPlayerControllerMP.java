package me.ht9.rose.mixin.mixins;

import me.ht9.rose.Rose;
import me.ht9.rose.event.events.PlayerDamageBlockEvent;
import me.ht9.rose.event.events.SyncCurrentItemEvent;
import me.ht9.rose.feature.module.modules.exploit.infdurability.InfDurability;
import me.ht9.rose.mixin.accessors.IBlock;
import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = PlayerControllerMP.class)
public abstract class MixinPlayerControllerMP
{
    @Shadow private int blockHitDelay;
    @Shadow protected abstract void syncCurrentPlayItem();

    @Unique private SyncCurrentItemEvent.Type type;

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
            if (event.cancelled()) ci.cancel();
        }
    }

    @Inject(
            method = "sendBlockRemoving",
            at = @At(
                    value = "HEAD"
            )
    )
    public void sendBlockRemoving(int j, int k, int l, int par4, CallbackInfo ci)
    {
        type = SyncCurrentItemEvent.Type.BREAK;
    }

    @Inject(
            method = "sendPlaceBlock",
            at = @At(
                    value = "HEAD"
            )
    )
    public void sendPlaceBlock(EntityPlayer world, World itemStack, ItemStack i, int j, int k, int l, int par7, CallbackInfoReturnable<Boolean> cir)
    {
        type = SyncCurrentItemEvent.Type.PLACE;
    }

    @Inject(
            method = "sendUseItem",
            at = @At(
                    value = "HEAD"
            )
    )
    public void sendUseItem(EntityPlayer world, World itemStack, ItemStack par3, CallbackInfoReturnable<Boolean> cir)
    {
        type = SyncCurrentItemEvent.Type.USE_ITEM;
    }

    @Inject(method = "syncCurrentPlayItem", at = @At("HEAD"), cancellable = true)
    public void syncCurrentPlayItem$Head(CallbackInfo ci)
    {
        SyncCurrentItemEvent event = new SyncCurrentItemEvent(type);
        Rose.bus().post(event);
        if (event.cancelled()) ci.cancel();
    }

    @Inject(
            method = "attackEntity",
            at = @At(
                    value = "HEAD"
            )
    )
    public void attackEntity(EntityPlayer entity, Entity par2, CallbackInfo ci)
    {
        type = SyncCurrentItemEvent.Type.ATTACK;
    }

    @Inject(
            method = "interactWithEntity",
            at = @At(
                    value = "HEAD"
            )
    )
    public void interactWithEntity(EntityPlayer entity, Entity par2, CallbackInfo ci)
    {
        type = SyncCurrentItemEvent.Type.INTERACT_ENTITY;
    }
}