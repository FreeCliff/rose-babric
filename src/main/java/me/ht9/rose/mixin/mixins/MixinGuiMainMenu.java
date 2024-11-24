package me.ht9.rose.mixin.mixins;

import me.ht9.rose.util.misc.GuiUsername;
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
        controlList.add(new GuiButton(421, this.width - 100 - 2, 2, 100, 20, "Change Username"));
    }

    @Inject(
            method = "actionPerformed",
            at = @At(
                    "RETURN"
            )
    )
    public void actionPerformed(GuiButton button, CallbackInfo ci)
    {
        if (button.enabled && button.id == 421)
            mc.displayGuiScreen(new GuiUsername((GuiMainMenu) (Object) this));
    }
}
