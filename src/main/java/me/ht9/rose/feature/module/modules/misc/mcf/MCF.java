package me.ht9.rose.feature.module.modules.misc.mcf;

import me.ht9.rose.event.bus.annotation.SubscribeEvent;
import me.ht9.rose.event.events.InputEvent;
import me.ht9.rose.feature.module.Module;
import me.ht9.rose.feature.module.annotation.Description;
import me.ht9.rose.feature.registry.Registry;
import net.minecraft.src.EntityOtherPlayerMP;
import net.minecraft.src.EnumMovingObjectType;
import org.lwjgl.input.Mouse;

@Description("Add or remove someone as friend by middle-clicking them.")
public final class MCF extends Module
{
    private static final MCF instance = new MCF();

    @SubscribeEvent
    public void onMouse(InputEvent.MouseInputEvent event)
    {
        if (
                mc.currentScreen == null
                && mc.inGameHasFocus
                && Mouse.getEventButton() == 2 && Mouse.getEventButtonState()
                && mc.objectMouseOver != null
                && mc.objectMouseOver.typeOfHit == EnumMovingObjectType.ENTITY
                && mc.objectMouseOver.entityHit instanceof EntityOtherPlayerMP player)
        {
            if (!Registry.friends().contains(player.username))
            {
                mc.ingameGUI.addChatMessage("Adding " + player.username + " to friends");
                Registry.friends().add(player.username);
            } else
            {
                mc.ingameGUI.addChatMessage("Removing " + player.username + " from friends");
                Registry.friends().remove(player.username);
            }
        }
    }

    public static MCF instance()
    {
        return instance;
    }
}
