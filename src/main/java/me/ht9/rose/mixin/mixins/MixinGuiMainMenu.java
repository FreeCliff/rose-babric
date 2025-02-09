package me.ht9.rose.mixin.mixins;

import me.ht9.rose.feature.gui.GuiProxy;
import me.ht9.rose.feature.gui.GuiSession;
import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiMainMenu;
import net.minecraft.src.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiMainMenu.class)
public class MixinGuiMainMenu extends GuiScreen
{
    @SuppressWarnings("unchecked")
    @Inject(
            method = "initGui",
            at = @At(
                    "RETURN"
            )
    )
    public void initGui(CallbackInfo ci)
    {
        controlList.add(new GuiButton(421, this.width - 75 - 2, 2, 75, 20, "Session"));
        controlList.add(new GuiButton(422, this.width - 75 - 2, 2 + 20 + 2, 75, 20, "Proxy"));
    }

    @Inject(
            method = "actionPerformed",
            at = @At(
                    "RETURN"
            )
    )
    public void actionPerformed(GuiButton button, CallbackInfo ci)
    {
        if (button.enabled)
        {
            switch (button.id)
            {
                case 421 -> mc.displayGuiScreen(new GuiSession(this));
                case 422 -> mc.displayGuiScreen(new GuiProxy(this));
            }
        }
    }
}
