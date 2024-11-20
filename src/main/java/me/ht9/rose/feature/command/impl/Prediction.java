package me.ht9.rose.feature.command.impl;

import me.ht9.rose.event.events.ChatGuiRenderEvent;
import me.ht9.rose.event.events.ChatKeyTypedEvent;
import me.ht9.rose.mixin.accessors.IGuiChat;
import org.apache.commons.lang3.tuple.Pair;
import org.spongepowered.include.com.google.common.base.Strings;
import me.ht9.rose.event.bus.annotation.SubscribeEvent;
import me.ht9.rose.feature.command.Command;
import me.ht9.rose.feature.registry.Registry;
import me.ht9.rose.util.Globals;
import net.minecraft.src.ScaledResolution;
import org.apache.commons.lang3.ArrayUtils;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;

public final class Prediction implements Globals
{
    private static final Prediction instance = new Prediction();

    private static String suggestion;

    @SubscribeEvent
    public void onChatGuiRender(ChatGuiRenderEvent event)
    {
        suggestion = null;
        if (event.text().startsWith(Registry.prefix()))
        {
            suggestion = findPossibleCompletion(event.text());
        }
    }

    @SubscribeEvent
    public void onChatKeyTyped(ChatKeyTypedEvent event)
    {
        if (event.keyCode() == Keyboard.KEY_TAB && suggestion != null)
        {
            ((IGuiChat) mc.currentScreen).setMessage(suggestion);
            event.setCancelled(true);
        }
    }

    private static String findPossibleCompletion(String inputText)
    {
        Command command = attemptedCommand(inputText);
        if (command != null)
        {
            if (inputText.contains("  ")) return null;
            String[] argsWorking = inputText.split(" ");
            if (command.suggestionMap().size() >= argsWorking.length - 1)
            {
                ScaledResolution sr = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
                if (argsWorking.length == 1 && !inputText.endsWith(" "))
                {
                    String start = argsWorking[0].substring(1);
                    mc.fontRenderer.drawStringWithShadow(command.name().toLowerCase().replaceFirst(start.toLowerCase(), ""), (int) (13.0F + mc.fontRenderer.getStringWidth(argsWorking[0])), (int) (sr.getScaledHeight() - 12.0F), -8355712);
                    return Registry.prefix() + command.name().toLowerCase();
                } else
                {
                    if (inputText.endsWith(" ") && command.suggestionMap().size() > argsWorking.length - 1) {
                        Pair<String, ArgumentSupplier> nextArg = command.suggestionMap().get(argsWorking.length - 1);
                        mc.fontRenderer.drawStringWithShadow(nextArg.getKey().toLowerCase(), (int) (16.0F + mc.fontRenderer.getStringWidth(inputText)), (int) (sr.getScaledHeight() - 13.0F), -8355712);
                        String[] next = nextArg.getValue().get();
                        if (ArrayUtils.isEmpty(next) || Strings.isNullOrEmpty(next[0]))
                        {
                            return null;
                        }
                        return inputText + nextArg.getValue().get()[0];
                    }
                    // Else
                    Pair<String, ArgumentSupplier> currentArg = command.suggestionMap().get(argsWorking.length - 2);
                    String working = argsWorking[argsWorking.length - 1];
                    String[] suggestions = currentArg.getValue().get();
                    String next = null;
                    for (String suggestion : suggestions)
                    {
                        if (suggestion.toLowerCase().startsWith(working.toLowerCase()))
                        {
                            next = suggestion;
                        }
                    }
                    if (next != null)
                    {
                        String sub = inputText.substring(0, inputText.length() - working.length());
                        mc.fontRenderer.drawStringWithShadow(next.toLowerCase().replaceFirst(working.toLowerCase(), ""), (int) (13.0F + mc.fontRenderer.getStringWidth(sub + working)), (int) (sr.getScaledHeight() - 12.0F), -8355712);
                        return sub + next;
                    }
                }
            }
        }
        return null;
    }

    private static Command attemptedCommand(String inputText)
    {
        if (inputText.equalsIgnoreCase(Registry.prefix() + " ")) return null;
        String[] words = inputText.split(" ");
        List<Command> commands = new ArrayList<>();

        Registry.commands()
                .stream()
                .filter(command -> command.name().toLowerCase().startsWith(words[0].substring(1).toLowerCase()))
                .forEach(commands::add);

        return commands.stream()
                .filter(command -> command.name().equalsIgnoreCase(words[0].substring(1))
                        || (words.length == 1 && !inputText.endsWith(" ")))
                .findFirst()
                .orElse(null);
    }

    public static Prediction instance()
    {
        return instance;
    }
}