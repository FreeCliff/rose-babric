package me.ht9.rose.mixin.mixins;

import me.ht9.rose.Rose;
import me.ht9.rose.event.events.BlockHitEvent;
import me.ht9.rose.event.events.InputEvent;
import me.ht9.rose.event.events.WorldChangeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.World;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MixinMinecraft
{
    @Shadow public MovingObjectPosition objectMouseOver;

    @Redirect(
            method = "runTick",
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/lwjgl/input/Mouse;next()Z"
            )
    )
    public boolean runTick$Mouse()
    {
        boolean result = Mouse.next();

        if (result)
        {
            Rose.bus().post(new InputEvent.MouseInputEvent());
        }

        return result;
    }

    @Redirect(
            method = "runTick",
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/lwjgl/input/Keyboard;next()Z"
            )
    )
    public boolean runTick$Keyboard()
    {
        boolean result = Keyboard.next();

        if (result)
        {
            Rose.bus().post(new InputEvent.KeyInputEvent());
        }

        return result;
    }

    @Inject(method = "changeWorld", at = @At("RETURN"))
    public void onWorldChange(World world, String entityPlayer, EntityPlayer par3, CallbackInfo ci) {
        if (world == null) return;
        Rose.bus().post(new WorldChangeEvent());
    }

    @Inject(method = "clickMouse", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/PlayerController;clickBlock(IIII)V"))
    public void clickBlock(int par1, CallbackInfo ci) {
        Rose.bus().post(new BlockHitEvent(objectMouseOver.blockX, objectMouseOver.blockY, objectMouseOver.blockZ, objectMouseOver.sideHit));
    }
}
