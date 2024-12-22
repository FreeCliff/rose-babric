package me.ht9.rose.mixin.accessors;

import net.minecraft.src.GuiChat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = GuiChat.class)
public interface IGuiChat
{
    @Accessor("message") void setMessage(String message);
}
