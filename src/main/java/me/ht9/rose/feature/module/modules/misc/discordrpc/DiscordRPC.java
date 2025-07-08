package me.ht9.rose.feature.module.modules.misc.discordrpc;

import me.ht9.rose.Rose;
import me.ht9.rose.event.bus.annotation.SubscribeEvent;
import me.ht9.rose.event.events.OpenScreenEvent;
import me.ht9.rose.event.events.TickEvent;
import me.ht9.rose.event.factory.Factory;
import me.ht9.rose.feature.gui.clickgui.RoseGui;
import me.ht9.rose.feature.module.Module;
import me.ht9.rose.feature.module.annotation.Description;
import me.ht9.rose.feature.module.setting.Setting;
import me.ht9.rose.util.module.Timer;
import meteordevelopment.discordipc.DiscordIPC;
import meteordevelopment.discordipc.RichPresence;
import net.minecraft.src.*;
import org.apache.commons.lang3.tuple.Pair;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Description("Enables Discord Rich Presence")
public final class DiscordRPC extends Module
{
    private static final DiscordRPC instance = new DiscordRPC();

    private final Setting<LineInfo> line1Left = new Setting<>("Line1Left", LineInfo.Username);
    private final Setting<LineInfo> line1Right = new Setting<>("Line1Right", LineInfo.Health);
    private final Setting<LineInfo> line2Left = new Setting<>("Line2Left", LineInfo.Dimension);
    private final Setting<LineInfo> line2Right = new Setting<>("Line2Right", LineInfo.IP);
    private final Setting<Boolean> allowCoords = new Setting<>("AllowCoords", false);
    private final Setting<Double> delay = new Setting<>("UpdateDelay", 0.2, .2, 2.0, 1);

    private static final RichPresence rpc = new RichPresence();
    private final Timer timer = new Timer();
    private boolean updateGuiStatus = false;

    private static final List<Pair<String, String>> customStates = new ArrayList<>();

    static
    {
        customStates.add(Pair.of("io.github.prospector.modmenu.gui.ModListScreen", "browsing mods"));
    }

    private DiscordRPC()
    {
        runInMainMenu = true;
    }

    @Override
    public void onEnable()
    {
        DiscordIPC.start(1280983858602573899L, () -> Rose.logger().info("Logged in account: {}", DiscordIPC.getUser().username));
        rpc.setStart(System.currentTimeMillis() / 1000L);
        rpc.setLargeImage("rose-babric", Rose.version());
    }

    @Override
    public void onDisable()
    {
        DiscordIPC.stop();
    }

    @SubscribeEvent
    private void onTick(TickEvent event)
    {
        if (!timer.hasReached(delay.value(), true)) return;
        if (!DiscordIPC.isConnected()) this.onEnable();

        if (this.canUpdate())
        {
            rpc.setDetails(getLine(line1Left.value()) + getSeparator(0) + getLine(line1Right.value()));
            rpc.setState(getLine(line2Left.value()) + getSeparator(1) + getLine(line2Right.value()));
        } else
        {
            if (updateGuiStatus)
            {
                rpc.setDetails("rose-babric " + Rose.version());

                if (mc.currentScreen instanceof GuiMainMenu) rpc.setState("looking at title screen");
                else if (mc.currentScreen instanceof GuiSelectWorld) rpc.setState("selecting world");
                else if (mc.currentScreen instanceof GuiCreateWorld) rpc.setState("creating world");
                else if (mc.currentScreen instanceof GuiMultiplayer) rpc.setState("selecting server");
                else if (mc.currentScreen instanceof GuiOptions || mc.currentScreen instanceof GuiVideoSettings || mc.currentScreen instanceof GuiControls) rpc.setState("changing options");
                else if (mc.currentScreen instanceof RoseGui) rpc.setState("browsing the clickgui");
                else if (mc.currentScreen instanceof GuiConnecting || mc.currentScreen instanceof GuiDownloadTerrain) rpc.setState("connecting to server");
                else
                {
                    boolean setState = false;
                    if (mc.currentScreen != null)
                    {
                        String className = mc.currentScreen.getClass().getName();
                        for (var pair : customStates)
                        {
                            if (className.equals(pair.getLeft()))
                            {
                                rpc.setState(pair.getRight());
                                setState = true;
                                break;
                            }
                        }
                    }
                    if (!setState) rpc.setState("in main menu");
                }
            }
        }

        DiscordIPC.setActivity(rpc);
        updateGuiStatus = !this.canUpdate();
    }

    private String getLine(LineInfo line)
    {
        return switch (line)
        {
            case Username -> mc.session.username;
            case Health -> mc.thePlayer == null ? "N/A" : (mc.thePlayer.health + " hp");
            case Version -> Rose.version();
            case World -> mc.theWorld == null ? "N/A" : (mc.theWorld.multiplayerWorld ? "multiplayer" : "singleplayer");
            case IP -> mc.theWorld == null ? "N/A" : (Factory.instance().ip + (Factory.instance().port != 25565 ? ":" + Factory.instance().port : ""));
            case Dimension -> mc.thePlayer == null ? "N/A" : (mc.thePlayer.dimension == 0 ? "overworld" : "nether");
            case CurrentItem -> mc.thePlayer == null || mc.thePlayer.inventory.getCurrentItem() == null ? "N/A" : (StringTranslate.getInstance().translateNamedKey(mc.thePlayer.inventory.getCurrentItem().getItemName()));
            case Coords -> (mc.thePlayer != null && allowCoords.value()) ? this.roundDouble(mc.thePlayer.posX, 1) + ", " + this.roundDouble(mc.thePlayer.posY, 1) + ", " + this.roundDouble(mc.thePlayer.posZ, 1) : "N/A";
            case None -> "";
        };
    }

    private String getSeparator(int line)
    {
        if (line == 0)
        {
            if (line1Left.value() == LineInfo.None || line1Right.value() == LineInfo.None) return "";
            else return " | ";
        } else
        {
            if (line2Left.value() == LineInfo.None || line2Right.value() == LineInfo.None) return "";
            else return " | ";
        }
    }

    @SubscribeEvent
    private void onOpenScreen(OpenScreenEvent event)
    {
        if (!this.canUpdate()) updateGuiStatus = true;
    }

    private boolean canUpdate()
    {
        return mc != null && mc.theWorld != null && mc.thePlayer != null;
    }

    private double roundDouble(double number, int scale)
    {
        return new BigDecimal(number).setScale(scale, RoundingMode.HALF_UP).doubleValue();
    }

    public static DiscordRPC instance()
    {
        return instance;
    }

    private enum LineInfo
    {
        Username, Health, Version, World, IP, Dimension, CurrentItem, Coords, None
    }
}
