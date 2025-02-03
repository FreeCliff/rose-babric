package me.ht9.rose.feature.module.modules.client.irc;

import me.ht9.rose.Rose;
import me.ht9.rose.event.bus.annotation.SubscribeEvent;
import me.ht9.rose.event.events.ChangeUsernameEvent;
import me.ht9.rose.event.events.ChatEvent;
import me.ht9.rose.feature.module.Module;
import me.ht9.rose.feature.module.annotation.Description;
import me.ht9.rose.feature.module.setting.Setting;
import me.ht9.rose.util.misc.FontColor;

import java.io.*;
import java.net.Socket;

@Description("Talk to other rose users.")
public final class IRC extends Module
{
    private static final IRC instance = new IRC();

    private final Setting<Mode> mode = new Setting<>("Mode", Mode.All);
    private final Setting<String> ircChannel = new Setting<>("Channel", "rose-babric");

    private Socket socket;
    private BufferedWriter writer;
    private BufferedReader reader;
    private String channel;

    private Thread listenerThread;

    private IRC()
    {
        setArrayListInfo(() -> "#" + ircChannel.value());
    }

    @Override
    public void onEnable()
    {
        if (mc.thePlayer == null && mc.theWorld == null)
            return;

        mc.ingameGUI.addChatMessage(FontColor.RED + "Connecting to Rose IRC, please wait a few seconds...");
        Rose.asyncExecutor().submit(() ->
        {
            try
            {
                socket = new Socket("irc.nathatpas.tel", 6667);
                writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String nick = mc.session.username;
                sendRawMessage("NICK " + nick);
                sendRawMessage("USER " + nick + " 8 * :Rose User");
                channel = "#" + ircChannel.value();
                System.out.println("IRC channel: " + channel);
                sendRawMessage("JOIN " + channel);
                mc.ingameGUI.addChatMessage(FontColor.GREEN + "Connected to Rose IRC!");
                listenerThread = new Thread(this::listenForMessages);
                listenerThread.start();
            } catch (Exception e)
            {
                mc.ingameGUI.addChatMessage(FontColor.RED + "Failed to connect to IRC.");
            }
        });
    }

    @Override
    public void onDisable()
    {
        try
        {
            if (writer != null)
            {
                sendRawMessage("QUIT :Leaving IRC");
                writer.close();
            }
            if (reader != null)
            {
                reader.close();
            }
            if (socket != null)
            {
                socket.close();
            }
            if (listenerThread != null)
            {
                listenerThread.interrupt();
            }
            mc.ingameGUI.addChatMessage(FontColor.RED + "Disconnected from the IRC.");
        }
        catch (IOException ignored)
        {
        }
    }

    @SubscribeEvent
    public void onUsernameChange(ChangeUsernameEvent event)
    {
        if (socket != null && socket.isConnected())
        {
            sendRawMessage("NICK " + mc.session.username);
        }
    }

    @SubscribeEvent
    public void onChat(ChatEvent event)
    {
        String message = event.message();
        if (mode.value().equals(Mode.Hashtag))
        {
            if (!message.startsWith("#"))
                return;

            event.setCancelled(true);
            sendMessage(message.substring(1));
        } else if (mode.value().equals(Mode.All))
        {
            if (message.startsWith("#"))
            {
                event.setMessage(message.substring(1));
                return;
            }

            event.setCancelled(true);
            sendMessage(message);
        }
    }

    public void sendMessage(String message)
    {
        if (socket == null || !socket.isConnected())
        {
            mc.ingameGUI.addChatMessage("Not connected to IRC, please re-enable the module.");
            return;
        }
        sendRawMessage("PRIVMSG " + channel + " :" + message);
        mc.ingameGUI.addChatMessage(FontColor.RED + "[IRC] " + FontColor.WHITE + "<" + mc.session.username + "> " + FontColor.GRAY + message);
    }

    private void sendRawMessage(String message)
    {
        try
        {
            if (writer != null)
            {
                writer.write(message + "\r\n");
                writer.flush();
            }
        }
        catch (IOException ignored)
        {
        }
    }

    private void listenForMessages()
    {
        try
        {
            String line;
            while ((line = reader.readLine()) != null)
            {
                processServerMessage(line);
                if (line.contains("005"))
                {
                    sendRawMessage("JOIN " + channel);
                }
            }
        }
        catch (IOException ignored)
        {
        }
    }

    private void processServerMessage(String line)
    {
        if (line.startsWith("PING"))
        {
            sendRawMessage("PONG " + line.substring(5));
        }
        else if (line.contains("PRIVMSG"))
        {
            String[] parts = line.split(":", 3);
            if (parts.length > 2)
            {
                String sender = parts[1].split("!")[0];
                String message = parts[2];
                mc.ingameGUI.addChatMessage(FontColor.RED + "[IRC] <" + sender + "> " + FontColor.GRAY + message);
            }
        }
        else if (line.contains("JOIN"))
        {
            String[] parts = line.split("!");
            if (parts.length > 0)
            {
                String nick = parts[0].substring(1);
                mc.ingameGUI.addChatMessage(FontColor.RED + "[IRC] " + FontColor.GREEN + nick + FontColor.GRAY + " joined the channel.");
            }
        }
        else if (line.contains("PART") || line.contains("QUIT"))
        {
            String[] parts = line.split("!");
            if (parts.length > 0)
            {
                String nick = parts[0].substring(1);
                mc.ingameGUI.addChatMessage(FontColor.RED + "[IRC] " + FontColor.GREEN + nick + FontColor.GRAY + " left the channel.");
            }
        }
        else if (line.contains("NICK"))
        {
            String[] parts = line.split("!");
            if (parts.length > 0)
            {
                String oldNick = parts[0].substring(1);
                String newNick = line.split("NICK :")[1].trim();
                mc.ingameGUI.addChatMessage(FontColor.RED + "[IRC] " + FontColor.GREEN + oldNick + FontColor.GRAY + " is now known as " + FontColor.GREEN + newNick);
            }
        }
    }

    public enum Mode
    {
        All,      // Capture all chat packets.
        Hashtag   // Capture only packets starting with a hashtag.
    }


    public static IRC instance()
    {
        return instance;
    }
}