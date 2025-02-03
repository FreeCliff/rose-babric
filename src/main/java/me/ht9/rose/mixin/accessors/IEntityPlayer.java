package me.ht9.rose.mixin.accessors;

import net.minecraft.src.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EntityPlayer.class)
public interface IEntityPlayer
{
    @Accessor("inPortal") boolean inPortal();
}
