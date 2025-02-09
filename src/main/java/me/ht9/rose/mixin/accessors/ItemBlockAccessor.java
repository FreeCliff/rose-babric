package me.ht9.rose.mixin.accessors;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import net.minecraft.src.ItemBlock;

@Mixin(ItemBlock.class)
public interface ItemBlockAccessor
{
    @Accessor("blockID") int blockId();
}
