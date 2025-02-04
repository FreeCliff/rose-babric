package me.ht9.rose.feature.module.modules.misc.inventorytweaks;

import me.ht9.rose.event.bus.annotation.SubscribeEvent;
import me.ht9.rose.event.events.PacketEvent;
import me.ht9.rose.feature.module.Module;
import me.ht9.rose.feature.module.annotation.Description;
import me.ht9.rose.feature.module.setting.Setting;
import net.minecraft.src.GuiInventory;
import net.minecraft.src.Packet101CloseWindow;

@Description("Various inventory related utilities")
public class InventoryTweaks extends Module
{
    private static final InventoryTweaks instance = new InventoryTweaks();

    public final Setting<Boolean> buttons = new Setting<>("Buttons", true);
    public final Setting<Integer> maxDropsPerTick = new Setting<>("MaxDropsPerTick", 1, 1, 50, this.buttons::value);

    public final Setting<Boolean> xCarry = new Setting<>("XCarry", false);

    @SubscribeEvent
    public void onPacket(PacketEvent event)
    {
        if (!this.xCarry.value()) return;
        if (!event.serverBound()) return;

        if (event.packet() instanceof Packet101CloseWindow && mc.currentScreen instanceof GuiInventory)
            event.setCancelled(true);
    }

    public static InventoryTweaks instance()
    {
        return instance;
    }
}
