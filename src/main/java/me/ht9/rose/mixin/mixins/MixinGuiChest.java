package me.ht9.rose.mixin.mixins;

import me.ht9.rose.event.factory.Factory;
import me.ht9.rose.feature.module.modules.exploit.instamine.Instamine;
import me.ht9.rose.feature.module.modules.misc.inventorytweaks.InventoryTweaks;
import me.ht9.rose.feature.module.modules.movement.freecam.Freecam;
import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(GuiChest.class)
public abstract class MixinGuiChest extends GuiContainer
{
    @Shadow private IInventory lowerChestInventory;

    @Unique private GuiButton dropAllButton;
    @Unique private boolean shouldDrop = false;

    public MixinGuiChest(Container container)
    {
        super(container);
    }

    @Override
    public void initGui()
    {
        super.initGui();
        if (InventoryTweaks.instance().buttons.value())
        {
            this.controlList.add(new GuiButton(0, 2, 2, 100, 20, "Dupe"));
            this.controlList.add(this.dropAllButton = new GuiButton(1, 2, 24, 100, 20, "Drop All"));
        }
    }

    @Override
    public void updateScreen()
    {
        super.updateScreen();
        this.dropAllButton.enabled = !this.shouldDrop;
        if (this.shouldDrop)
        {
            boolean hasFound = false;
            int drops = 0;
            for (int i = 0; i < this.lowerChestInventory.getSizeInventory(); i++)
            {
                if (this.lowerChestInventory.getStackInSlot(i) != null)
                {
                    mc.playerController.func_27174_a(this.inventorySlots.windowId, i, 0, false, mc.thePlayer);
                    mc.playerController.func_27174_a(this.inventorySlots.windowId, -999, 0, false, mc.thePlayer);
                    hasFound = true;
                    if (++drops >= InventoryTweaks.instance().maxDropsPerTick.value())
                        break;
                }
            }
            if (!hasFound)
                this.shouldDrop = false;
        }
    }

    @Override
    protected void actionPerformed(GuiButton guiButton)
    {
        super.actionPerformed(guiButton);
        if (InventoryTweaks.instance().buttons.value())
        {
            if (guiButton.id == 0)
            {
                if (mc.getSendQueue() == null || Factory.instance().lastChestCoords == null)
                    return;
                int[] lastChestCoords = Factory.instance().lastChestCoords;
                Freecam.instance().enable();
                Instamine.instance().mineBlock(lastChestCoords[0], lastChestCoords[1], lastChestCoords[2], lastChestCoords[3]);
            } else if (guiButton.id == 1)
            {
                this.shouldDrop = true;
            }
        }
    }
}
