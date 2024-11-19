package me.ht9.rose.feature.command.commands;

import me.ht9.rose.feature.command.impl.Executable;
import me.ht9.rose.feature.registry.Registry;
import me.ht9.rose.util.Globals;
import me.ht9.rose.util.misc.FontColor;

public final class FriendsCommand extends Executable implements Globals
{
    @Override
    public void accept(String[] args)
    {
        if (args.length == 0)
        {
            mc.ingameGUI.addChatMessage(FontColor.RED + Registry.prefix() + "friends <add/del/list> [name]");
            return;
        }

        if (args[0].equalsIgnoreCase("list"))
        {
            String friends = String.join(", ", Registry.friends());

            mc.ingameGUI.addChatMessage("Friends " + FontColor.GRAY + "(" + Registry.friends().size() + ")" + FontColor.WHITE + ": " + friends);
        }
        else if (args[0].equalsIgnoreCase("add"))
        {
            if (args.length < 2)
            {
                mc.ingameGUI.addChatMessage(FontColor.RED + Registry.prefix() + "friends add <name>");
                return;
            }
            Registry.friends().add(args[1]);
            mc.ingameGUI.addChatMessage("Added " + args[1] + " to friends.");
        }
        else if (args[0].equalsIgnoreCase("del"))
        {
            if (args.length < 2)
            {
                mc.ingameGUI.addChatMessage(FontColor.RED + Registry.prefix() + "friends del <name>");
                return;
            }
            boolean foundFriend = false;
            for (String friend : Registry.friends())
            {
                if (friend.toLowerCase().startsWith(args[1].toLowerCase()))
                {
                    Registry.friends().remove(friend);
                    mc.ingameGUI.addChatMessage("Removed " + args[1] + " from friends.");
                    foundFriend = true;
                }
            }
            if (!foundFriend)
            {
                mc.ingameGUI.addChatMessage(FontColor.RED + "Couldn't find " + args[1] + " in friends list!");
            }
        }
    }
}
