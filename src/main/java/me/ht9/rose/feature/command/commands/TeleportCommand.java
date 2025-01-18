package me.ht9.rose.feature.command.commands;

import me.ht9.rose.feature.command.impl.Executable;
import me.ht9.rose.feature.registry.Registry;
import me.ht9.rose.util.Globals;
import me.ht9.rose.util.misc.FontColor;
import net.minecraft.src.Packet11PlayerPosition;

public final class TeleportCommand extends Executable implements Globals {
    @Override
    public void accept(String[] args) {
        if (mc.thePlayer != null && args.length == 3) {
            try {
                double x = Double.parseDouble(args[0]);
                double y = Double.parseDouble(args[1]);
                double z = Double.parseDouble(args[2]);
                if (mc.getSendQueue() != null)
                    mc.getSendQueue().addToSendQueue(new Packet11PlayerPosition(x, y, y + (mc.thePlayer.posY - mc.thePlayer.boundingBox.minY), z, mc.thePlayer.onGround));
                mc.thePlayer.setPosition(x, y, z);
            }
            catch (NumberFormatException e)
            {
                mc.ingameGUI.addChatMessage(FontColor.RED + "Not a valid number!");
            }
        }
        else
        {
            mc.ingameGUI.addChatMessage(FontColor.RED + Registry.prefix() + "teleport <x> <y> <z>");
        }
    }
}
