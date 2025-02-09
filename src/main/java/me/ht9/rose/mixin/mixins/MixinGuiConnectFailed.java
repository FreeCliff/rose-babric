package me.ht9.rose.mixin.mixins;

import me.ht9.rose.event.factory.Factory;
import me.ht9.rose.feature.module.modules.misc.autoreconnect.AutoReconnect;
import me.ht9.rose.util.misc.FontColor;
import me.ht9.rose.util.module.Timer;
import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiConnectFailed;
import net.minecraft.src.GuiConnecting;
import net.minecraft.src.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(GuiConnectFailed.class)
public class MixinGuiConnectFailed extends GuiScreen
{
    @Unique private final Timer timer = new Timer();
    @Unique private GuiButton button;

    @Inject(method = "<init>", at = @At("RETURN"))
    public void init$Return(String string2, String objects, Object[] par3, CallbackInfo ci)
    {
        this.timer.reset();
    }

    @Inject(method = "initGui", at = @At("RETURN"))
    public void initGui$Return(CallbackInfo ci)
    {
        this.button = new GuiButton(new Random().nextInt(100, 999), width / 2 - 100, height / 4 + 120 + 12 + 20 + 2, "Auto Reconnect");
        this.controlList.add(this.button);
    }

    @Inject(method = "updateScreen", at = @At("HEAD"))
    public void updateScreen$Head(CallbackInfo ci)
    {
        if (!AutoReconnect.instance().enabled())
        {
            button.displayString = "Auto Reconnect";
            this.timer.reset();
            return;
        }

        if (this.timer.hasReached(AutoReconnect.instance().delay.value()))
        {
            mc.displayGuiScreen(new GuiConnecting(mc, Factory.instance().ip, Factory.instance().port));
        }

        button.displayString = String.format("Auto Reconnect " + FontColor.RED + "(%.1f)", AutoReconnect.instance().delay.value() - timer.time());
    }

    @Inject(method = "actionPerformed", at = @At("RETURN"))
    public void actionPerformed$Return(GuiButton button, CallbackInfo ci)
    {
        if (button.equals(this.button))
        {
            AutoReconnect.instance().toggle();
        }
    }
}
