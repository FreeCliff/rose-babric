package me.ht9.rose.feature.module.modules.misc.chatbomb;

import me.ht9.rose.event.bus.annotation.SubscribeEvent;
import me.ht9.rose.event.events.TickEvent;
import me.ht9.rose.feature.module.Module;
import me.ht9.rose.feature.module.annotation.Description;
import me.ht9.rose.feature.module.setting.Setting;
import net.minecraft.src.ChatAllowedCharacters;
import net.minecraft.src.Packet3Chat;

import java.util.Random;

@Description("Chat spammer")
public final class ChatBomb extends Module
{
    private static final ChatBomb instance = new ChatBomb();
    private static final String allowedChars = ChatAllowedCharacters.allowedCharacters;
    private static final Random random = new Random();

    private final Setting<Integer> amount = new Setting<>("Amount", 1, 1, 100);

    @SuppressWarnings("unused")
    @SubscribeEvent
    public void onTick(TickEvent event)
    {
        if (mc.getSendQueue() == null) return;
        for (int h = 0; h < amount.value(); h++)
        {
            StringBuilder spamString = new StringBuilder();
            for (int i = 0; i < 100; i++)
            {
                char randomChar = allowedChars.charAt(random.nextInt(allowedChars.length()));
                spamString.append(randomChar);
            }
            mc.getSendQueue().addToSendQueue(new Packet3Chat(spamString.toString()));
        }
    }

    public static ChatBomb instance()
    {
        return instance;
    }
}
