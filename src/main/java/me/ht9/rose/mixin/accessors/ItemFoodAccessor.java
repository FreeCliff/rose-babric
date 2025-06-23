package me.ht9.rose.mixin.accessors;

import net.minecraft.src.ItemFood;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ItemFood.class)
public interface ItemFoodAccessor
{
    @Accessor("healAmount") int healAmount();
}
