package me.ht9.rose.feature.command.commands;

import me.ht9.rose.feature.command.impl.Executable;
import me.ht9.rose.feature.registry.Registry;
import me.ht9.rose.util.Globals;
import me.ht9.rose.util.misc.FontColor;
import net.minecraft.src.Packet3Chat;
import org.apache.commons.lang3.ArrayUtils;

public final class SpamCommand extends Executable implements Globals {
    @Override
    public void accept(String[] args) {
        if (mc.thePlayer != null && args.length >= 1) {
            try {
                for (int i = 0; i < Integer.parseInt(args[0]); i++)
                {
                    mc.getSendQueue().addToSendQueue(new Packet3Chat(String.join(" ", ArrayUtils.remove(args, 0))));
                }
            } catch (NumberFormatException ignored) {}
        }
        else
        {
            mc.ingameGUI.addChatMessage(FontColor.RED + Registry.prefix() + "spam <amount> <message>");
        }
    }
}
