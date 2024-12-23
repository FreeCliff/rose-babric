package me.ht9.rose.feature.command.commands;

import me.ht9.rose.feature.command.impl.Executable;
import me.ht9.rose.feature.registry.Registry;
import me.ht9.rose.util.Globals;
import me.ht9.rose.util.misc.FontColor;

public final class VclipCommand extends Executable implements Globals {
    @Override
    public void accept(String[] args) {
        if (mc.thePlayer != null && args.length == 1) {
            try {
                mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + Double.parseDouble(args[0]), mc.thePlayer.posZ);
            } catch (NumberFormatException ignored) {}
        }
        else
        {
            mc.ingameGUI.addChatMessage(FontColor.RED + Registry.prefix() + "vclip <y>");
        }
    }
}
