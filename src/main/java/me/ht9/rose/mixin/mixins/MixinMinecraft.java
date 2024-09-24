package me.ht9.rose.mixin.mixins;

import me.ht9.rose.Rose;
import me.ht9.rose.event.events.InputEvent;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Minecraft.class)
public class MixinMinecraft
{
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
}
