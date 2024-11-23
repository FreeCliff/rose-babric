package me.ht9.rose.feature.module.modules.client.irc;

import me.ht9.rose.Rose;
import me.ht9.rose.event.bus.annotation.SubscribeEvent;
import me.ht9.rose.event.events.ChangeUsernameEvent;
import me.ht9.rose.event.events.PacketEvent;
import me.ht9.rose.feature.module.Module;
import me.ht9.rose.feature.module.annotation.Description;
import me.ht9.rose.util.misc.FontColor;
import net.engio.mbassy.listener.Handler;
import net.minecraft.src.Packet3Chat;
import org.kitteh.irc.client.library.Client;
import org.kitteh.irc.client.library.event.channel.ChannelJoinEvent;
import org.kitteh.irc.client.library.event.channel.ChannelMessageEvent;
import org.kitteh.irc.client.library.event.user.UserNickChangeEvent;
import org.kitteh.irc.client.library.event.user.UserQuitEvent;
import org.kitteh.irc.client.library.util.HostWithPort;

@Description("Talk to other rose users.")
public final class IRC extends Module
{
    private static final IRC instance = new IRC();

    private Client client;
    private String channel;

    @Override
    public void onEnable()
    {
        if (mc.thePlayer == null && mc.theWorld == null)
            return;

        mc.ingameGUI.addChatMessage(FontColor.RED + "Connecting to Rose IRC, please wait a few seconds...");
        Rose.asyncExecutor().submit(() -> {
            client = Client.builder()
                    .nick(mc.session.username)
                    .server()
                    .address(HostWithPort.of("irc.nathatpas.tel", 6697))
                    .then()
                    .buildAndConnect();
            client.getEventManager().registerEventListener(new Listener());
            channel = "#rose-babric";
            client.addChannel(channel);
        });
    }

    @Override
    public void onDisable()
    {
        if (client == null)
            return;

        client.shutdown();
        mc.ingameGUI.addChatMessage(FontColor.RED + "Disconnected from the IRC.");

    }

    @SubscribeEvent
    public void onUsernameChange(ChangeUsernameEvent event)
    {
        if (client == null)
            return;

        client.setNick(mc.session.username);
    }

    @SubscribeEvent
    public void onPacket(PacketEvent event)
    {
        if (event.packet() instanceof Packet3Chat packet)
        {
            if (!event.serverBound())
                return;

            if (!packet.message.startsWith("#"))
                return;

            event.setCancelled(true);
            this.sendMessage(packet.message.substring(1));
        }
    }

    public void sendMessage(String message)
    {
        if (client == null)
        {
            mc.ingameGUI.addChatMessage("Not connected to IRC, please re-enable the module.");
            return;
        }
        client.sendMessage(channel, message);
        mc.ingameGUI.addChatMessage(FontColor.RED + "[IRC] " + FontColor.WHITE + "<" + client.getNick() + "> " + FontColor.GRAY + message);
    }

    private static class Listener
    {
        @Handler
        public void onChannelJoinEvent(ChannelJoinEvent event)
        {
            if (event.getUser().getNick().equalsIgnoreCase(mc.session.username))
            {
                mc.ingameGUI.addChatMessage(FontColor.GREEN + "Successfully connected to the IRC.");
            }
            mc.ingameGUI.addChatMessage(FontColor.RED + "[IRC] " + event.getUser().getNick() + FontColor.GRAY + " just entered the IRC.");
        }

        @Handler
        public void onUserQuitEvent(UserQuitEvent event)
        {
            mc.ingameGUI.addChatMessage(FontColor.RED + "[IRC] " + event.getUser().getNick() + FontColor.GRAY + " just left the IRC.");
        }

        @Handler
        public void onChannelMessage(ChannelMessageEvent event)
        {
            mc.ingameGUI.addChatMessage(FontColor.RED + "[IRC] <" + event.getActor().getNick() + "> " + FontColor.GRAY + event.getMessage());
        }

        @Handler
        public void onUserNickChange(UserNickChangeEvent event)
        {
            mc.ingameGUI.addChatMessage(FontColor.RED + "[IRC] " + event.getOldUser().getNick() + FontColor.GRAY + " is now known as " + FontColor.RED + event.getNewUser().getNick());
        }
    }

    public static IRC instance()
    {
        return instance;
    }
}
