package me.ht9.rose.mixin.mixins;

import me.ht9.rose.Rose;
import me.ht9.rose.event.events.PushByEvent;
import me.ht9.rose.event.events.SignEditEvent;
import me.ht9.rose.feature.module.modules.misc.portals.Portals;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = EntityPlayerSP.class)
public abstract class MixinEntityPlayerSP extends EntityPlayer
{
    @Shadow protected Minecraft mc;

    public MixinEntityPlayerSP(World arg)
    {
        super(arg);
    }

    @Inject(
            method = "pushOutOfBlocks",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    public void pushOutOfBlocks$Head(double x, double y, double z, CallbackInfoReturnable<Boolean> cir)
    {
        PushByEvent event = new PushByEvent(this, PushByEvent.Pusher.BLOCK);
        Rose.bus().post(event);
        if (event.cancelled())
        {
            cir.setReturnValue(false);
            cir.cancel();
        }
    }

    @Redirect(method = "onLivingUpdate", at = @At(value = "FIELD", target = "Lnet/minecraft/client/Minecraft;currentScreen:Lnet/minecraft/src/GuiScreen;"))
    public GuiScreen getCurrentScreenProxy(Minecraft instance)
    {
        if (Portals.instance().enabled()) return null;
        return instance.currentScreen;
    }

    @Inject(method = "displayGUIEditSign", at = @At("HEAD"), cancellable = true)
    private void displayGUIEditSign$Head(TileEntitySign sign, CallbackInfo ci)
    {
        SignEditEvent event = new SignEditEvent(SignEditEvent.Type.START, sign);
        Rose.bus().post(event);
        if (event.cancelled())
        {
            if (mc.theWorld.multiplayerWorld)
                mc.getSendQueue().addToSendQueue(new Packet130UpdateSign(sign.xCoord, sign.yCoord, sign.zCoord, sign.signText));
            ci.cancel();
        }
    }
}