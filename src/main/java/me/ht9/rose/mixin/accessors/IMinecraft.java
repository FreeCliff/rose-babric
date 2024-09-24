package me.ht9.rose.mixin.accessors;

import net.minecraft.client.Minecraft;
import net.minecraft.src.Timer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = Minecraft.class)
public interface IMinecraft
{
    @Accessor(value = "timer")
    Timer timer();
}