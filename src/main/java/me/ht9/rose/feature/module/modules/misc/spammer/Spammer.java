package me.ht9.rose.feature.module.modules.misc.spammer;

import me.ht9.rose.Rose;
import me.ht9.rose.event.bus.annotation.SubscribeEvent;
import me.ht9.rose.event.events.TickEvent;
import me.ht9.rose.feature.module.Module;
import me.ht9.rose.feature.module.annotation.Description;
import me.ht9.rose.feature.module.setting.Setting;
import me.ht9.rose.util.config.FileUtils;
import me.ht9.rose.util.module.Timer;
import net.minecraft.src.ChatAllowedCharacters;
import net.minecraft.src.Packet3Chat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Description("Chat spammer")
public final class Spammer extends Module
{
    private static final Spammer instance = new Spammer();
    private static final String allowedChars = ChatAllowedCharacters.allowedCharacters;
    private static final Random random = new Random();

    private final Timer timer = new Timer();
    private final List<String> messages = new ArrayList<>();
    private int messageIndex = 0;

    private final Setting<Mode> mode = new Setting<>("Mode", Mode.Random)
            .withOnChange(value ->
            {
                if (value.equals(Mode.File))
                {
                    updateMessages();
                }
            });

    private final Setting<Integer> randomLength = new Setting<>("Length", 1, 100, 100, () -> mode.value().equals(Mode.Random));

    private final Setting<String> fileName = new Setting<>("File", "spam.txt", () -> mode.value().equals(Mode.File))
            .withOnChange(value -> updateMessages());
    private final Setting<Boolean> fillRest = new Setting<>("Fill Rest", false, () -> mode.value().equals(Mode.File));

    private final Setting<Double> delay = new Setting<>("Delay", 0.0, 0.0, 2.0, 3);
    private final Setting<Integer> amount = new Setting<>("Amount", 1, 1, 100);

    private Spammer()
    {
        setArrayListInfo(() -> mode.value().toString());
    }

    @Override
    public void onEnable() {
        timer.reset();
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public void onTick(TickEvent event)
    {
        if (mc.getSendQueue() == null) return;
        if (!timer.hasReached(delay.value(), true)) return;
        for (int h = 0; h < amount.value(); h++)
        {
            String spamString = "";

            if (mode.value().equals(Mode.Random))
            {
                spamString = getRandom(randomLength.value());
            }
            else if (mode.value().equals(Mode.File))
            {
                if (messageIndex >= messages.size())
                {
                    messageIndex = 0;
                }
                String msg = messages.get(messageIndex++);

                if (fillRest.value())
                {
                    if (msg.length() < 99)
                    {
                        msg = msg + " " + getRandom(99 - msg.length());
                    }
                }

                spamString = msg;
            }

            if (!spamString.isEmpty())
            {
                mc.getSendQueue().addToSendQueue(new Packet3Chat(spamString));
            }
        }
    }

    private String getRandom(int length)
    {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++)
        {
            char randomChar = allowedChars.charAt(random.nextInt(allowedChars.length()));
            stringBuilder.append(randomChar);
        }
        return stringBuilder.toString();
    }

    private void updateMessages()
    {
        try
        {
            messages.clear();

            File file = new File(FileUtils.MAIN_FOLDER, fileName.value());
            if (file.exists())
            {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line;
                while ((line = reader.readLine()) != null)
                {
                    messages.add(line);
                }
            }
        }
        catch (Throwable t)
        {
            Rose.logger().error("Failed to load spam file {}", fileName.value(), t);
        }
    }

    public static Spammer instance()
    {
        return instance;
    }

    public enum Mode
    {
        Random,
        File
    }
}
