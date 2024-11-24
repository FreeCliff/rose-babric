package me.ht9.rose.mixin.accessors;

import net.minecraft.src.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Block.class)
public interface IBlock
{
    @Accessor("blockHardness") float getBlockHardness();
}
