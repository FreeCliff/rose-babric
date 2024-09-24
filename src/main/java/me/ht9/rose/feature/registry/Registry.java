package me.ht9.rose.feature.registry;

import me.ht9.rose.Rose;
import me.ht9.rose.feature.command.Command;
import me.ht9.rose.feature.command.commands.LagbackCommand;
import me.ht9.rose.feature.command.impl.CommandBuilder;
import me.ht9.rose.feature.command.commands.SpawnCmd;
import me.ht9.rose.feature.module.modules.client.clickgui.ClickGUI;
import me.ht9.rose.feature.module.modules.movement.freecam.Freecam;
import me.ht9.rose.feature.module.setting.Setting;
import me.ht9.rose.feature.module.Module;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public final class Registry
{
    private static final List<Module> modules = new ArrayList<>();
    private static final List<Command> commands = new ArrayList<>();

    private static final String prefix = String.valueOf('.');

    public static void loadModules()
    {
        modules.add(ClickGUI.instance());

        modules.add(Freecam.instance());

        modules.forEach(module ->
        {
            for (Field field : module.getClass().getDeclaredFields())
            {
                if (Setting.class.isAssignableFrom(field.getType()))
                {
                    try
                    {
                        field.setAccessible(true);
                        module.settings().add((Setting<?>) field.get(module));
                    } catch (Throwable t)
                    {
                        Rose.logger().error("Failed to instantiate setting {}", field.getName());
                    }
                }
            }
            module.settings().add(module.drawn());
            module.settings().add(module.bindMode());
            module.settings().add(module.toggleBind());
        });
    }

    public static void loadCommands()
    {
        commands.add(new CommandBuilder("Lagback")
                .withDescription("Send bed leave to force a lagback.")
                .withExecutable(new LagbackCommand())
                .asCommand());
        commands.add(new CommandBuilder("Spawn")
                .withDescription("Send an invalid position packet to teleport you to spawn on bukkit servers.")
                .withExecutable(new SpawnCmd())
                .asCommand());
    }

    public static String prefix()
    {
        return prefix;
    }

    public static List<Module> modules()
    {
        return modules;
    }

    public static List<Command> commands()
    {
        return commands;
    }
}
