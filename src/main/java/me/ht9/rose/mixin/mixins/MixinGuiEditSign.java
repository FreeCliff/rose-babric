package me.ht9.rose.mixin.mixins;

import me.ht9.rose.Rose;
import me.ht9.rose.event.events.SignEditEvent;
import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiEditSign;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.TileEntitySign;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiEditSign.class)
public abstract class MixinGuiEditSign extends GuiScreen
{
    @Shadow private TileEntitySign entitySign;
    @Shadow protected abstract void actionPerformed(GuiButton guiButton);

    @Inject(method = "actionPerformed", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/TileEntitySign;onInventoryChanged()V"))
    private void actionPerformed$finish(GuiButton par1, CallbackInfo ci)
    {
        SignEditEvent event = new SignEditEvent(SignEditEvent.Type.FINISH, this.entitySign);
        Rose.bus().post(event);
    }
}
