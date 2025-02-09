package me.ht9.rose.feature.gui;

import me.ht9.rose.Rose;
import me.ht9.rose.event.events.ChatGuiRenderEvent;
import me.ht9.rose.event.events.ChatKeyTypedEvent;
import net.minecraft.src.ChatAllowedCharacters;
import net.minecraft.src.GuiScreen;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;

public final class GuiCustomChat extends GuiScreen
{
    private static final String ALLOWED_CHARACTERS = ChatAllowedCharacters.allowedCharacters;

    private String message = "";
    private int updateCounter = 0;
    private int chatHistoryPosition;
    private static final List<String> CHAT_HISTORY = new ArrayList<>();

    private void setTextFromHistory()
    {
        this.message = CHAT_HISTORY.get(CHAT_HISTORY.size() + this.chatHistoryPosition);
    }

    @Override
    public void initGui()
    {
        Keyboard.enableRepeatEvents(true);
    }

    @Override
    public void onGuiClosed()
    {
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    public void updateScreen()
    {
        this.updateCounter++;
    }

    @Override
    protected void keyTyped(char c, int i)
    {
        ChatKeyTypedEvent event = new ChatKeyTypedEvent(i, this.message);
        Rose.bus().post(event);

        if (i == Keyboard.KEY_ESCAPE)
        {
            mc.displayGuiScreen(null);
        } else if (c == '\r')
        {
            int size = CHAT_HISTORY.size();
            if (!(size > 0 && CHAT_HISTORY.get(size - 1).equals(this.message))) {
                CHAT_HISTORY.add(this.message);
            }

            mc.thePlayer.sendChatMessage(this.message.trim());
            mc.displayGuiScreen(null);
        } else if (i == Keyboard.KEY_UP && this.chatHistoryPosition > -CHAT_HISTORY.size())
        {
            this.chatHistoryPosition--;
            this.setTextFromHistory();
        } else if (i == Keyboard.KEY_DOWN && this.chatHistoryPosition < -1)
        {
            this.chatHistoryPosition++;
            this.setTextFromHistory();
        } else if (c == 22)
        {
            String clipboard = getClipboardString();
            if (clipboard == null) clipboard = "";

            int length = Math.min(100 - this.message.length(), clipboard.length());

            if (length > 0)
                this.message = this.message + clipboard.substring(0, length);
        } else
        {
            if (i == Keyboard.KEY_BACK && !this.message.isEmpty())
            {
                this.message = this.message.substring(0, this.message.length() - 1);
            }

            if (ALLOWED_CHARACTERS.indexOf(c) >= 0 && this.message.length() < 100)
            {
                this.message = this.message + c;
            }
        }
    }

    @Override
    public void drawScreen(int i, int j, float f)
    {
        this.drawRect(2, this.height - 14, this.width - 2, this.height - 2, Integer.MIN_VALUE);

        ChatGuiRenderEvent event = new ChatGuiRenderEvent(this.message);
        Rose.bus().post(event);
        this.drawString(this.fontRenderer, "> " + event.text() + (this.updateCounter / 6 % 2 == 0 ? "_" : ""), 4, this.height - 12, 0xE0E0E0);
        super.drawScreen(i, j, f);
    }
}
