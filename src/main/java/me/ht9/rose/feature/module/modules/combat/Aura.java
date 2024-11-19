package me.ht9.rose.feature.module.modules.combat;

import me.ht9.rose.event.bus.annotation.SubscribeEvent;
import me.ht9.rose.event.events.PosRotUpdateEvent;
import me.ht9.rose.feature.module.Module;
import me.ht9.rose.feature.module.annotation.Description;
import me.ht9.rose.feature.module.setting.Setting;
import net.minecraft.src.*;

@Description("Attacks entities near you")
public final class Aura extends Module
{
    private static final Aura instance = new Aura();

    private final Setting<Boolean> players = new Setting<>("Players", true);
    private final Setting<Boolean> animals = new Setting<>("Animals", false);
    private final Setting<Boolean> mobs = new Setting<>("Mobs", true);
    private final Setting<Double> range = new Setting<>("Range", 1.0, 7.0, 15.0, 1);

    @SuppressWarnings("unused")
    @SubscribeEvent
    public void onUpdate(PosRotUpdateEvent event)
    {
        for (Object object : mc.theWorld.loadedEntityList)
        {
            if (!(object instanceof Entity entity)) continue;
            if (entity.equals(mc.thePlayer)) continue;

            if (
                    (entity instanceof EntityPlayer && players.value())
                    || ((entity instanceof EntityAnimal || entity instanceof EntityWaterMob) && animals.value())
                    || ((entity instanceof EntityMob || entity instanceof EntityFlying) && mobs.value())
            )
            {
                if (mc.thePlayer.ticksExisted % 2 == 0 && mc.thePlayer.getDistanceToEntity(entity) <= range.value())
                {
                    mc.playerController.attackEntity(mc.thePlayer, entity);
                }
            }
        }
    }

    public static Aura instance()
    {
        return instance;
    }
}
