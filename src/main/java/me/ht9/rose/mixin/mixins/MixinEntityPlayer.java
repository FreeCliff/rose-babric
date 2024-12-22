package me.ht9.rose.mixin.mixins;

import net.minecraft.src.*;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static me.ht9.rose.util.Globals.mc;

@Mixin(EntityPlayer.class)
public abstract class MixinEntityPlayer extends EntityLiving
{
    @Shadow public abstract ItemStack getCurrentEquippedItem();

    @Unique private int originalStackSize;

    public MixinEntityPlayer(World world)
    {
        super(world);
    }

    @Inject(
            method = "attackTargetEntityWithCurrentItem",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/src/ItemStack;stackSize:I",
                    opcode = Opcodes.GETFIELD
            )
    )
    public void setStackSizeForAttack(Entity entity, CallbackInfo ci)
    {
        if (mc.isMultiplayerWorld())
        {
            ItemStack item = this.getCurrentEquippedItem();
            this.originalStackSize = item.stackSize;
            item.stackSize = Math.max(item.stackSize, 1);
        }
    }
    @Inject(
            method = "attackTargetEntityWithCurrentItem",
            at = @At("RETURN")
    )
    public void resetStackSizeForAttack(Entity entity, CallbackInfo ci)
    {
        if (mc.isMultiplayerWorld())
        {
            this.getCurrentEquippedItem().stackSize = this.originalStackSize;
        }
    }

    @Inject(
            method = "useCurrentItemOnEntity",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/src/ItemStack;stackSize:I",
                    opcode = Opcodes.GETFIELD
            )
    )
    public void setStackSizeForUse(Entity par1, CallbackInfo ci)
    {
        if (mc.isMultiplayerWorld())
        {
            ItemStack item = this.getCurrentEquippedItem();
            this.originalStackSize = item.stackSize;
            item.stackSize = Math.max(item.stackSize, 1);
        }
    }
    @Inject(
            method = "useCurrentItemOnEntity",
            at = @At("RETURN")
    )
    public void resetStackSizeForUse(Entity par1, CallbackInfo ci)
    {
        if (mc.isMultiplayerWorld())
        {
            this.getCurrentEquippedItem().stackSize = this.originalStackSize;
        }
    }
}
