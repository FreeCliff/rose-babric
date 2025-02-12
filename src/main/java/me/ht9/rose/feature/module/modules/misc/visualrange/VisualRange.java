package me.ht9.rose.feature.module.modules.misc.visualrange;

import me.ht9.rose.event.bus.annotation.SubscribeEvent;
import me.ht9.rose.event.events.EntityEvent;
import me.ht9.rose.feature.module.Module;
import me.ht9.rose.util.misc.FontColor;
import net.minecraft.src.EntityPlayer;

public final class VisualRange extends Module
{
    private static final VisualRange instance = new VisualRange();

    private VisualRange()
    {
        setArrayListInfo(() ->
        {
            if (mc.theWorld != null)
            {
                return String.valueOf(mc.theWorld.playerEntities.size() - 1);
            }
            return "";
        });
    }

    @SubscribeEvent
    public void onEntityJoin(EntityEvent.EntityJoinEvent event)
    {
        if (event.entity() instanceof EntityPlayer player)
        {
            mc.ingameGUI.addChatMessage( FontColor.LIGHT_PURPLE + player.username + " has entered your visual range!");
        }
    }

    @SubscribeEvent
    public void onEntityLeave(EntityEvent.EntityLeaveEvent event)
    {
        if (event.entity() instanceof EntityPlayer player)
        {
            mc.ingameGUI.addChatMessage(FontColor.LIGHT_PURPLE + player.username + " has left your visual range!");
        }
    }

    public static VisualRange instance()
    {
        return instance;
    }
}
