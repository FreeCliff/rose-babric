package me.ht9.rose.mixin.mixins;

import net.minecraft.src.*;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderItem.class)
public class MixinRenderItem
{
    @Inject(
            method = "renderItemOverlayIntoGUI",
            at = @At(
                    "HEAD"
            )
    )
    public void renderItemOverlayIntoGUI(FontRenderer fontRenderer, RenderEngine renderEngine, ItemStack itemStack, int i, int j, CallbackInfo ci)
    {
        if (itemStack == null) return;
        if (itemStack.stackSize >= 1) return;

        String var6 = "" + itemStack.stackSize;
        GL11.glDisable(2896);
        GL11.glDisable(2929);
        fontRenderer.drawStringWithShadow(var6, i + 19 - 2 - fontRenderer.getStringWidth(var6), j + 6 + 3, 16777215);
        GL11.glEnable(2896);
        GL11.glEnable(2929);
    }
}
