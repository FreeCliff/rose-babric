package me.ht9.rose.mixin.mixins;

import me.ht9.rose.Rose;
import me.ht9.rose.event.events.ChatGuiRenderEvent;
import me.ht9.rose.event.events.ChatKeyTypedEvent;
import net.minecraft.src.GuiChat;
import net.minecraft.src.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = GuiChat.class)
public class MixinGuiChat extends GuiScreen
{
    @Shadow private int updateCounter = 0;
    @Shadow protected String message = "";

    @ModifyArg(
            method = "drawScreen",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/src/GuiChat;drawString(Lnet/minecraft/src/FontRenderer;Ljava/lang/String;III)V"
            ),
            index = 1
    )
    private String drawStringText(String text)
    {
        ChatGuiRenderEvent event = new ChatGuiRenderEvent(text.replaceFirst("^> ", "").replaceFirst("_$", ""));
        Rose.bus().post(event);
        return "> " + event.text() + (updateCounter / 6 % 2 == 0 ? "_" : "");
    }

    @Inject(
            method = "keyTyped",
            at = @At(
                    "HEAD"
            )
    )
    private void keyTyped(char typedChar, int keyCode, CallbackInfo ci)
    {
        ChatKeyTypedEvent event = new ChatKeyTypedEvent(keyCode, this.message);
        Rose.bus().post(event);
    }
}
