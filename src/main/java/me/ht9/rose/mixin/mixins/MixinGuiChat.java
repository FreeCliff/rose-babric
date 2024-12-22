package me.ht9.rose.mixin.mixins;

import me.ht9.rose.Rose;
import me.ht9.rose.event.events.ChatGuiRenderEvent;
import me.ht9.rose.event.events.ChatKeyTypedEvent;
import net.minecraft.src.GuiChat;
import net.minecraft.src.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = GuiChat.class)
public class MixinGuiChat extends GuiScreen
{
    @Unique private int updateCounter = 0;
    @Shadow protected String message = "";

    /**
     * goyslop signamfix 30/09/2024
     */
    @Inject(
            method = "updateScreen",
            at = @At(
                    "HEAD"
            )
    )
    private void updateScreen(CallbackInfo ci)
    {
        updateCounter++;
    }

    @Inject(
            method = "drawScreen",
            at = @At("HEAD"),
            cancellable = true
    )
    private void drawScreen(int j, int f, float par3, CallbackInfo ci)
    {
        ci.cancel();
        ChatGuiRenderEvent event = new ChatGuiRenderEvent(this.message);
        Rose.bus().post(event);
        this.drawRect(2, this.height - 14, this.width - 2, this.height - 2, Integer.MIN_VALUE);
        fontRenderer.drawStringWithShadow("> " + event.text() + (this.updateCounter / 6 % 2 == 0 ? "_" : ""), 4, this.height - 12, 14737632);
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
