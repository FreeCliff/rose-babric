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

    @Inject(method = "initGui", at = @At("TAIL"))
    private void initGui$Tail(CallbackInfo ci)
    {
        SignEditEvent event = new SignEditEvent(SignEditEvent.Type.START, this.entitySign);
        event.setLine1(this.entitySign.signText[0]);
        event.setLine2(this.entitySign.signText[1]);
        event.setLine3(this.entitySign.signText[2]);
        event.setLine4(this.entitySign.signText[3]);
        Rose.bus().post(event);
        this.entitySign.signText[0] = event.line1();
        this.entitySign.signText[1] = event.line2();
        this.entitySign.signText[2] = event.line3();
        this.entitySign.signText[3] = event.line4();
        if (event.cancelled())
            this.actionPerformed((GuiButton) this.controlList.get(0));
    }

    @Inject(method = "actionPerformed", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/TileEntitySign;onInventoryChanged()V"))
    private void actionPerformed$finish(GuiButton par1, CallbackInfo ci)
    {
        SignEditEvent event = new SignEditEvent(SignEditEvent.Type.FINISH, this.entitySign);
        event.setLine1(this.entitySign.signText[0]);
        event.setLine2(this.entitySign.signText[1]);
        event.setLine3(this.entitySign.signText[2]);
        event.setLine4(this.entitySign.signText[3]);
        Rose.bus().post(event);
        this.entitySign.signText[0] = event.line1();
        this.entitySign.signText[1] = event.line2();
        this.entitySign.signText[2] = event.line3();
        this.entitySign.signText[3] = event.line4();
    }
}
