package me.ht9.rose.mixin.mixins;

import me.ht9.rose.Rose;
import me.ht9.rose.event.events.RenderPlayerLabelEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.RenderManager;
import net.minecraft.src.RenderPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderPlayer.class)
public class MixinRenderPlayer {
    @Inject(
            method = "passSpecialRender(Lnet/minecraft/src/EntityPlayer;DDD)V",
            at = @At(
                    "HEAD"
            ),
            cancellable = true
    )
    public void passSpecialRender(EntityPlayer player, double x, double y, double z, CallbackInfo ci)
    {
        if (!Minecraft.isGuiEnabled() || player == RenderManager.instance.livingPlayer) return;
        RenderPlayerLabelEvent event = new RenderPlayerLabelEvent(player, player.username, x, y, z);
        Rose.bus().post(event);
        if (event.cancelled())
            ci.cancel();
    }
}
