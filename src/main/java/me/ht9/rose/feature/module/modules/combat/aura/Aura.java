package me.ht9.rose.feature.module.modules.combat.aura;

import me.ht9.rose.event.bus.annotation.SubscribeEvent;
import me.ht9.rose.event.events.PosRotUpdateEvent;
import me.ht9.rose.event.events.ItemRenderEvent;
import me.ht9.rose.feature.module.Module;
import me.ht9.rose.feature.module.annotation.Description;
import me.ht9.rose.feature.module.setting.Setting;
import me.ht9.rose.feature.registry.Registry;
import net.minecraft.src.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

@Description("Attacks entities near you")
public final class Aura extends Module
{
    private static final Aura instance = new Aura();

    private final Setting<Boolean> multi = new Setting<>("Multi", true);
    private final Setting<Boolean> rotate = new Setting<>("Rotate", true);
    private final Setting<Boolean> swing = new Setting<>("Swing", false);
    private final Setting<Boolean> fakeblock = new Setting<>("Fake Block", false)
            .withDescription("Doesn't work on StAPI, fuck you minediver");

    private final Setting<Boolean> players = new Setting<>("Players", true);
    private final Setting<Boolean> animals = new Setting<>("Animals", false);
    private final Setting<Boolean> mobs = new Setting<>("Mobs", true);
    private final Setting<Double> range = new Setting<>("Range", 1.0, 7.0, 15.0, 1);
    private final Setting<Double> aps = new Setting<>("APS", 1.0, 10.0, 10.0);

    private boolean shouldFakeBlock = false;

    private Aura()
    {
        setArrayListInfo(() ->
        {
            String info = "";

            if (players.value()) info += "P";
            if (mobs.value()) info += "M";
            if (animals.value()) info += "A";

            return info;
        });
    }

    @SubscribeEvent
    public void onUpdate(PosRotUpdateEvent event)
    {
        boolean hasRotated = false;
        List<EntityLiving> entities = new ArrayList<>();
        for (Object object : mc.theWorld.loadedEntityList)
        {
            if (object instanceof EntityLiving entity)
            {
                if (entity instanceof EntityPlayerSP || entity instanceof EntityClientPlayerMP) continue;
                if (entity.health == 0) continue;
                if (mc.thePlayer.getDistanceToEntity(entity) > range.value()) continue;

                if (
                        (entity instanceof EntityPlayer player && players.value() && !Registry.friends().contains(player.username))
                                || ((entity instanceof EntityAnimal || entity instanceof EntityWaterMob) && animals.value())
                                || ((entity instanceof EntityMob || entity instanceof EntityFlying || entity instanceof EntitySlime) && mobs.value())
                )
                {
                    entities.add(entity);
                    if (!this.multi.value()) break;
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

        shouldFakeBlock = false;

        for (Entity entity : entities)
        {
            shouldFakeBlock = true;

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

            if (mc.thePlayer.ticksExisted % (20.0 / aps.value()) == 0)
            {
                if (swing.value())
                    mc.thePlayer.swingItem();

                mc.playerController.attackEntity(mc.thePlayer, entity);
            }
        }
    }

    @SubscribeEvent
    public void onItemRender(ItemRenderEvent event)
    {
        if (shouldFakeBlock && fakeblock.value() && event.entity().equals(mc.thePlayer) && event.itemStack().getItem() instanceof ItemSword)
        {
            glTranslatef(-0.5f, 0.2f, 0.0f);
            glRotatef(30.0f, 0.0f, 1.0f, 0.0f);
            glRotatef(-80.0f, 1.0f, 0.0f, 0.0f);
            glRotatef(60.0f, 0.0f, 1.0f, 0.0f);
        }
    }

    public static Aura instance()
    {
        return instance;
    }
}
