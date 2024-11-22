package me.ht9.rose.feature.module.modules.combat;

import me.ht9.rose.event.bus.annotation.SubscribeEvent;
import me.ht9.rose.event.events.PosRotUpdateEvent;
import me.ht9.rose.feature.module.Module;
import me.ht9.rose.feature.module.annotation.Description;
import me.ht9.rose.feature.module.setting.Setting;
import net.minecraft.src.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Description("Attacks entities near you")
public final class Aura extends Module
{
    private static final Aura instance = new Aura();

    private final Setting<Boolean> rotate = new Setting<>("Rotate", true);
    private final Setting<Boolean> swing = new Setting<>("Swing", false);

    private final Setting<Boolean> players = new Setting<>("Players", true);
    private final Setting<Boolean> animals = new Setting<>("Animals", false);
    private final Setting<Boolean> mobs = new Setting<>("Mobs", true);
    private final Setting<Double> range = new Setting<>("Range", 1.0, 7.0, 15.0, 1);

    @SuppressWarnings("unused")
    @SubscribeEvent
    public void onUpdate(PosRotUpdateEvent event)
    {
        boolean hasRotated = false;
        List<Entity> entities = new ArrayList<>();
        for (Object object : mc.theWorld.loadedEntityList)
        {
            if (object instanceof Entity entity)
            {
                if (entity instanceof EntityPlayerSP) continue;
                if (mc.thePlayer.getDistanceToEntity(entity) > range.value()) continue;
                if (
                        (entity instanceof EntityPlayer && players.value())
                                || ((entity instanceof EntityAnimal || entity instanceof EntityWaterMob) && animals.value())
                                || ((entity instanceof EntityMob || entity instanceof EntityFlying) && mobs.value())
                )
                {
                    entities.add(entity);
                }
            }
        }

        entities.sort(Comparator.comparingDouble(entity ->
        {
            double dX = entity.posX - mc.thePlayer.posX;
            double dY = entity.posY + entity.getEyeHeight() - mc.thePlayer.posY;
            double dZ = entity.posZ - mc.thePlayer.posZ;
            return dX * dX + dY * dY + dZ * dZ;
        }));

        for (Entity entity : entities)
        {
            if (rotate.value() && !hasRotated)
            {
                double dX = entity.posX - mc.thePlayer.posX;
                double dY = entity.posY + entity.getEyeHeight() - mc.thePlayer.posY;
                double dZ = entity.posZ - mc.thePlayer.posZ;
                double distance = Math.sqrt(dX * dX + dY * dY + dZ * dZ);

                float yaw = (float) (Math.atan2(dZ, dX) * (180 / Math.PI)) - 90;
                float pitch = (float) -(Math.atan2(dY, distance) * (180 / Math.PI));

                event.setYaw(yaw);
                event.setPitch(pitch);
                event.setModelRotations();

                hasRotated = true;
            }

            if (mc.thePlayer.ticksExisted % 2 == 0)
            {
                if (swing.value())
                    mc.thePlayer.swingItem();

                mc.playerController.attackEntity(mc.thePlayer, entity);
            }
        }
    }

    public static Aura instance()
    {
        return instance;
    }
}
