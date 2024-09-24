package me.ht9.rose.feature.command.commands;

import me.ht9.rose.feature.command.impl.Executable;
import me.ht9.rose.util.Globals;
import net.minecraft.src.Packet19EntityAction;

public final class LagbackCommand  extends Executable implements Globals
{
    @Override
    public void accept(String[] args)
    {
        if (args.length == 0)
        {
            mc.getSendQueue().addToSendQueue(new Packet19EntityAction(mc.thePlayer, 3));
        }
    }
}
