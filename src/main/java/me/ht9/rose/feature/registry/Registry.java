package me.ht9.rose.feature.registry;

import me.ht9.rose.Rose;
import me.ht9.rose.feature.Feature;
import me.ht9.rose.feature.command.Command;
import me.ht9.rose.feature.command.commands.*;
import me.ht9.rose.feature.command.impl.CommandBuilder;
import me.ht9.rose.feature.module.modules.client.background.Background;
import me.ht9.rose.feature.module.modules.client.clickgui.ClickGUI;
import me.ht9.rose.feature.module.modules.client.hidelogin.HideLogin;
import me.ht9.rose.feature.module.modules.client.hud.Hud;
import me.ht9.rose.feature.module.modules.client.irc.IRC;
import me.ht9.rose.feature.module.modules.client.mainmenu.MainMenu;
import me.ht9.rose.feature.module.modules.client.togglemsg.ToggleMsg;
import me.ht9.rose.feature.module.modules.combat.aura.Aura;
import me.ht9.rose.feature.module.modules.exploit.boattravel.BoatTravel;
import me.ht9.rose.feature.module.modules.exploit.fastportal.FastPortal;
import me.ht9.rose.feature.module.modules.exploit.infdurability.InfDurability;
import me.ht9.rose.feature.module.modules.exploit.instamine.Instamine;
import me.ht9.rose.feature.module.modules.exploit.lawnmower.Lawnmower;
import me.ht9.rose.feature.module.modules.exploit.newchunks.NewChunks;
import me.ht9.rose.feature.module.modules.exploit.nuker.Nuker;
import me.ht9.rose.feature.module.modules.exploit.packetlogger.PacketLogger;
import me.ht9.rose.feature.module.modules.exploit.packetmine.PacketMine;
import me.ht9.rose.feature.module.modules.exploit.sneak.Sneak;
import me.ht9.rose.feature.module.modules.misc.autoeat.AutoEat;
import me.ht9.rose.feature.module.modules.misc.autoreconnect.AutoReconnect;
import me.ht9.rose.feature.module.modules.misc.autosign.AutoSign;
import me.ht9.rose.feature.module.modules.misc.autotnt.AutoTNT;
import me.ht9.rose.feature.module.modules.misc.discordrpc.DiscordRPC;
import me.ht9.rose.feature.module.modules.misc.fastplace.FastPlace;
import me.ht9.rose.feature.module.modules.misc.inventorytweaks.InventoryTweaks;
import me.ht9.rose.feature.module.modules.misc.mcf.MCF;
import me.ht9.rose.feature.module.modules.misc.portals.Portals;
import me.ht9.rose.feature.module.modules.misc.retard.Retard;
import me.ht9.rose.feature.module.modules.misc.tcpnodelay.TcpNoDelay;
import me.ht9.rose.feature.module.modules.misc.timer.Timer;
import me.ht9.rose.feature.module.modules.misc.spammer.Spammer;
import me.ht9.rose.feature.module.modules.misc.visualrange.VisualRange;
import me.ht9.rose.feature.module.modules.movement.autowalk.AutoWalk;
import me.ht9.rose.feature.module.modules.movement.faststop.FastStop;
import me.ht9.rose.feature.module.modules.movement.flight.Flight;
import me.ht9.rose.feature.module.modules.movement.freecam.Freecam;
import me.ht9.rose.feature.module.modules.movement.guiwalk.GuiMove;
import me.ht9.rose.feature.module.modules.movement.jesus.Jesus;
import me.ht9.rose.feature.module.modules.movement.noclip.NoClip;
import me.ht9.rose.feature.module.modules.movement.nofall.NoFall;
import me.ht9.rose.feature.module.modules.movement.norotate.NoRotate;
import me.ht9.rose.feature.module.modules.movement.scaffold.Scaffold;
import me.ht9.rose.feature.module.modules.movement.speed.Speed;
import me.ht9.rose.feature.module.modules.movement.step.Step;
import me.ht9.rose.feature.module.modules.movement.velocity.Velocity;
import me.ht9.rose.feature.module.modules.movement.yaw.Yaw;
import me.ht9.rose.feature.module.modules.render.cameratweaks.CameraTweaks;
import me.ht9.rose.feature.module.modules.render.esp.ESP;
import me.ht9.rose.feature.module.modules.render.fov.FOV;
import me.ht9.rose.feature.module.modules.render.fullbright.FullBright;
import me.ht9.rose.feature.module.modules.render.nametags.NameTags;
import me.ht9.rose.feature.module.modules.render.nooverlay.NoOverlay;
import me.ht9.rose.feature.module.modules.render.norender.NoRender;
import me.ht9.rose.feature.module.modules.render.xray.Xray;
import me.ht9.rose.feature.module.setting.Setting;
import me.ht9.rose.feature.module.Module;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public final class Registry
{
    private static final List<Module> modules = new ArrayList<>();
    private static final List<Command> commands = new ArrayList<>();

    private static final List<String> friends = new CopyOnWriteArrayList<>();

    private static final String prefix = String.valueOf('.');

    public static void loadModules()
    {
        modules.add(Background.instance());
        modules.add(ClickGUI.instance());
        modules.add(HideLogin.instance());
        modules.add(Hud.instance());
        modules.add(IRC.instance());
        modules.add(MainMenu.instance());
        modules.add(ToggleMsg.instance());

        modules.add(Aura.instance());

        modules.add(BoatTravel.instance());
        modules.add(FastPortal.instance());
        modules.add(InfDurability.instance());
        modules.add(Instamine.instance());
        modules.add(Lawnmower.instance());
        modules.add(NewChunks.instance());
        modules.add(Nuker.instance());
        modules.add(PacketLogger.instance());
        modules.add(PacketMine.instance());
        modules.add(Sneak.instance());

        modules.add(AutoEat.instance());
        modules.add(AutoReconnect.instance());
        modules.add(AutoSign.instance());
        modules.add(AutoTNT.instance());
        modules.add(DiscordRPC.instance());
        modules.add(FastPlace.instance());
        modules.add(InventoryTweaks.instance());
        modules.add(MCF.instance());
        modules.add(Portals.instance());
        modules.add(Spammer.instance());
        modules.add(Retard.instance());
        modules.add(TcpNoDelay.instance());
        modules.add(Timer.instance());
        modules.add(VisualRange.instance());

        modules.add(AutoWalk.instance());
        modules.add(FastStop.instance());
        modules.add(Flight.instance());
        modules.add(Freecam.instance());
        modules.add(GuiMove.instance());
        modules.add(Jesus.instance());
        modules.add(NoClip.instance());
        modules.add(NoFall.instance());
        modules.add(NoRotate.instance());
        modules.add(Scaffold.instance());
        modules.add(Speed.instance());
        modules.add(Step.instance());
        modules.add(Velocity.instance());
        modules.add(Yaw.instance());

        modules.add(CameraTweaks.instance());
        modules.add(ESP.instance());
        modules.add(FOV.instance());
        modules.add(FullBright.instance());
        modules.add(NameTags.instance());
        modules.add(NoOverlay.instance());
        modules.add(NoRender.instance());
        modules.add(Xray.instance());
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
                .withSuggestion("<y>", () -> new String[]{})
                .asCommand());
        commands.add(new CommandBuilder("Friends")
                .withDescription("Modify the friends list")
                .withExecutable(new FriendsCommand())
                .withSuggestion("<add/del/list>", () -> new String[]{"add", "del", "list"})
                .withSuggestion("[name]", () -> Registry.friends().toArray(new String[0]))
                .asCommand());
        commands.add(new CommandBuilder("Spam")
                .withDescription("Spam a message multiple times")
                .withExecutable(new SpamCommand())
                .withSuggestion("<amount>", () -> new String[]{})
                .asCommand());
        commands.add(new CommandBuilder("Crash")
                .withDescription("Vehicle crash exploit")
                .withExecutable(new CrashCommand())
                .asCommand());
        commands.add(new CommandBuilder("Teleport")
                .withDescription("Teleport somewhere")
                .withExecutable(new TeleportCommand())
                .asCommand());
    }

    public static void finishLoad()
    {
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

        modules.sort(Comparator.comparing(Feature::name));
        commands.sort(Comparator.comparing(Command::name));
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

    public static List<String> friends()
    {
        return friends;
    }
}
