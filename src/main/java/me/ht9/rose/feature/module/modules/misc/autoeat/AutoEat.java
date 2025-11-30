package me.ht9.rose.feature.module.modules.misc.autoeat;

import me.ht9.rose.event.bus.annotation.SubscribeEvent;
import me.ht9.rose.event.events.PosRotUpdateEvent;
import me.ht9.rose.feature.module.Module;
import me.ht9.rose.feature.module.annotation.Description;
import me.ht9.rose.feature.module.setting.Setting;
import me.ht9.rose.mixin.accessors.ItemFoodAccessor;
import me.ht9.rose.util.module.Timer;
import net.minecraft.src.ItemFood;

@Description("Here you go anon")
public final class AutoEat extends Module
{
    private static final AutoEat instance = new AutoEat();

    private final Setting<Integer> health = new Setting<>("Health", 1, 15, 20);
    private final Setting<Double> delay = new Setting<>("Delay", 0.0, 0.5, 2.0, 1);
    private final Setting<Boolean> untilFull = new Setting<>("UntilFull", false);

    private final Timer timer = new Timer();

    @SubscribeEvent
    public void onUpdate(PosRotUpdateEvent event)
    {
        if (!timer.hasReached(delay.value(), true)) return;
        if (mc.thePlayer.health >= health.value()) return;

        int foodSlot = this.getFoodSlot();
        if (foodSlot == -1) return;

        int times = 1;
        if (untilFull.value())
        {
            int healAmount = ((ItemFoodAccessor) mc.thePlayer.inventory.mainInventory[foodSlot].getItem()).healAmount();

            int needed = 20 - mc.thePlayer.health;
            times = needed / healAmount;
            if (needed % healAmount != 0) times++;
        }

        int oldSlot = mc.thePlayer.inventory.currentItem;
        mc.thePlayer.inventory.currentItem = foodSlot;
        for (int i = 0; i < times; i++)
            mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getCurrentEquippedItem());
        mc.thePlayer.inventory.currentItem = oldSlot;
    }

    private int getFoodSlot()
    {
        for (int i = 0; i < 9; i++)
            if (mc.thePlayer.inventory.mainInventory[i] != null && mc.thePlayer.inventory.mainInventory[i].getItem() instanceof ItemFood)
                return i;
        return -1;
    }

    public static AutoEat instance()
    {
        return instance;
    }
}
