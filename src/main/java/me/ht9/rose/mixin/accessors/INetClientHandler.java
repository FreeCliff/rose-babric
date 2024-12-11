package me.ht9.rose.mixin.accessors;

import net.minecraft.src.NetClientHandler;
import net.minecraft.src.NetworkManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(NetClientHandler.class)
public interface INetClientHandler
{
    @Accessor("netManager") NetworkManager netManager();
}
