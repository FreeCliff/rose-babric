package me.ht9.rose.mixin.mixins;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EntityPlayer.class)
public class MixinEntityPlayer
{
    @Redirect(
            method = "attackTargetEntityWithCurrentItem",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/src/ItemStack;stackSize:I",
                    opcode = Opcodes.GETFIELD
            )
    )
    public int fixAttackWithUnderstackedItem(ItemStack instance)
    {
        return Math.max(instance.stackSize, 1);
    }

    @Redirect(
            method = "useCurrentItemOnEntity",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/src/ItemStack;stackSize:I",
                    opcode = Opcodes.GETFIELD
            )
    )
    public int fixUseWithUnderstackedItem(ItemStack instance)
    {
        return Math.max(instance.stackSize, 1);
    }
}
