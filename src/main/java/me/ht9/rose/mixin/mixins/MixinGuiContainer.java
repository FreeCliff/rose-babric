package me.ht9.rose.mixin.mixins;

import me.ht9.rose.event.factory.Factory;
import me.ht9.rose.feature.module.modules.exploit.instamine.Instamine;
import me.ht9.rose.feature.module.modules.movement.freecam.Freecam;
import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiContainer.class)
public class MixinGuiContainer extends GuiScreen
{
    @SuppressWarnings("unchecked")
    @Inject(method = "initGui", at = @At("HEAD"))
    public void initGui(CallbackInfo ci)
    {
        if ((Object) this instanceof GuiChest)
            controlList.add(new GuiButton(0, 2, 2, 100, 20, "Dupe"));
    }

    @Override
    protected void actionPerformed(GuiButton guiButton) {
        if (guiButton.id == 0)
        {
            if (mc.getSendQueue() == null || Factory.instance().lastChestCoords == null)
                return;
            int[] lastChestCoords = Factory.instance().lastChestCoords;
            Freecam.instance().enable();
            Instamine.instance().mineBlock(lastChestCoords[0], lastChestCoords[1], lastChestCoords[2], lastChestCoords[3]);
        }
    }
}
