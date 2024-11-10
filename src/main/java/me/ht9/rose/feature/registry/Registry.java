package me.ht9.rose.feature.registry;

import me.ht9.rose.Rose;
import me.ht9.rose.feature.command.Command;
import me.ht9.rose.feature.command.commands.LagbackCommand;
import me.ht9.rose.feature.command.commands.VclipCommand;
import me.ht9.rose.feature.command.impl.CommandBuilder;
import me.ht9.rose.feature.command.commands.SpawnCmd;
import me.ht9.rose.feature.module.modules.client.clickgui.ClickGUI;
import me.ht9.rose.feature.module.modules.client.hudeditor.HudEditor;
import me.ht9.rose.feature.module.modules.exploit.boattravel.BoatTravel;
import me.ht9.rose.feature.module.modules.exploit.instamine.Instamine;
import me.ht9.rose.feature.module.modules.exploit.lawnmower.Lawnmower;
import me.ht9.rose.feature.module.modules.exploit.packetlogger.PacketLogger;
import me.ht9.rose.feature.module.modules.exploit.packetmine.PacketMine;
import me.ht9.rose.feature.module.modules.exploit.timer.Timer;
import me.ht9.rose.feature.module.modules.movement.flight.Flight;
import me.ht9.rose.feature.module.modules.movement.freecam.Freecam;
import me.ht9.rose.feature.module.modules.movement.noclip.NoClip;
import me.ht9.rose.feature.module.modules.movement.nofall.NoFall;
import me.ht9.rose.feature.module.modules.movement.scaffold.Scaffold;
import me.ht9.rose.feature.module.modules.movement.speed.Speed;
import me.ht9.rose.feature.module.modules.movement.velocity.Velocity;
import me.ht9.rose.feature.module.modules.movement.yaw.Yaw;
import me.ht9.rose.feature.module.modules.render.fullbright.FullBright;
import me.ht9.rose.feature.module.modules.render.nooverlay.NoOverlay;
import me.ht9.rose.feature.module.modules.render.norender.NoRender;
import me.ht9.rose.feature.module.modules.render.xray.Xray;
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
        modules.add(HudEditor.instance());

        modules.add(BoatTravel.instance());
        modules.add(Instamine.instance());
        modules.add(Lawnmower.instance());
        modules.add(PacketLogger.instance());
        modules.add(PacketMine.instance());
        modules.add(Timer.instance());

        modules.add(Flight.instance());
        modules.add(Freecam.instance());
        modules.add(NoClip.instance());
        modules.add(NoFall.instance());
        modules.add(Scaffold.instance());
        modules.add(Speed.instance());
        modules.add(Velocity.instance());
        modules.add(Yaw.instance());

        modules.add(FullBright.instance());
        modules.add(NoOverlay.instance());
        modules.add(NoRender.instance());
        modules.add(Xray.instance());

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
        commands.add(new CommandBuilder("Vclip")
                .withDescription("Teleport up and down")
                .withExecutable(new VclipCommand())
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
