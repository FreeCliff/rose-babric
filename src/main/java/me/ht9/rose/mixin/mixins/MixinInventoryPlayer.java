package me.ht9.rose.mixin.mixins;

import me.ht9.rose.feature.module.modules.render.cameratweaks.CameraTweaks;
import me.ht9.rose.util.Globals;
import net.minecraft.src.InventoryPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryPlayer.class)
public class MixinInventoryPlayer
{
    @Inject(method = "changeCurrentItem", at = @At("HEAD"), cancellable = true)
    public void changeCurrentItem$Head(int mouseWheel, CallbackInfo ci)
    {
        if (Globals.mc.gameSettings.thirdPersonView && CameraTweaks.instance().enabled() && CameraTweaks.instance().scrolling.value())
            ci.cancel();
    }
}
