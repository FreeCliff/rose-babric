package me.ht9.rose.mixin.mixins;

import me.ht9.rose.Rose;
import me.ht9.rose.event.events.Render2dEvent;
import me.ht9.rose.feature.gui.GuiCustomChat;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = GuiIngame.class)
public abstract class MixinGuiIngame
{
    @Inject(
            method = "renderGameOverlay",
            at = @At(
                    value = "TAIL"
            )
    )
    public void renderText(float f, boolean flag, int i, int j, CallbackInfo ci)
    {
        Rose.bus().post(new Render2dEvent(f));
    }

    @Redirect(method = "renderGameOverlay", at = @At(value = "FIELD", target = "Lnet/minecraft/client/Minecraft;currentScreen:Lnet/minecraft/src/GuiScreen;", opcode = Opcodes.GETFIELD))
    private GuiScreen currentScreen(Minecraft instance)
    {
        return instance.currentScreen instanceof GuiCustomChat ? new GuiChat() : instance.currentScreen;
    }
}