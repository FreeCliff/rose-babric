package me.ht9.rose.mixin.mixins;

import me.ht9.rose.event.factory.Factory;
import net.minecraft.client.Minecraft;
import net.minecraft.src.GuiConnecting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiConnecting.class)
public class MixinGuiConnecting
{
    @Inject(method = "<init>", at = @At("RETURN"))
    public void init$Return(Minecraft mc, String ip, int port, CallbackInfo ci)
    {
        Factory.instance().ip = ip;
        Factory.instance().port = port;
    }
}
