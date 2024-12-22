package me.ht9.rose.mixin.mixins;

import me.ht9.rose.Rose;
import me.ht9.rose.event.events.PushByEvent;
import me.ht9.rose.feature.module.modules.exploit.sneak.Sneak;
import me.ht9.rose.feature.module.modules.movement.freecam.Freecam;
import net.minecraft.client.Minecraft;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityPlayerSP;
import net.minecraft.src.World;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = EntityPlayerSP.class)
public class MixinEntityPlayerSP extends EntityPlayer
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
    public void pushOutOfBlocks(double x, double y, double z, CallbackInfoReturnable<Boolean> cir)
    {
        PushByEvent event = new PushByEvent(this, PushByEvent.Pusher.BLOCK);
        Rose.bus().post(event);
        if (event.cancelled() || Freecam.instance().enabled())
        {
            cir.setReturnValue(false);
            cir.cancel();
        }
    }

    @Inject(method = "isSneaking", at = @At("HEAD"), cancellable = true)
    public void isSneaking$Head(CallbackInfoReturnable<Boolean> cir)
    {
        if (!Thread.currentThread().getStackTrace()[3].getMethodName().equals("moveEntity")) return;
        if (Sneak.instance().enabled() && !Keyboard.isKeyDown(mc.gameSettings.keyBindSneak.keyCode))
        {
            cir.setReturnValue(false);
            cir.cancel();
        }
    }

    @Override
    public void func_6420_o()
    {
    }
}