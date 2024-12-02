package me.ht9.rose.event.factory;

import me.ht9.rose.event.bus.annotation.SubscribeEvent;
import me.ht9.rose.event.events.*;
import me.ht9.rose.feature.command.Command;
import me.ht9.rose.feature.gui.RoseGui;
import me.ht9.rose.feature.module.keybinding.Bind;
import me.ht9.rose.feature.module.modules.client.clickgui.ClickGUI;
import me.ht9.rose.feature.module.modules.client.mainmenu.MainMenu;
import me.ht9.rose.feature.module.setting.Setting;
import me.ht9.rose.feature.registry.Registry;
import me.ht9.rose.feature.module.Module;
import me.ht9.rose.util.Globals;
import me.ht9.rose.util.misc.MultiprotoIntegrationHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.src.*;
import org.apache.commons.lang3.ArrayUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

@SuppressWarnings("unused")
public class Factory implements Globals
{
    private static final Factory instance = new Factory();

    public float rotationRenderPitch;
    public float prevRotationRenderPitch;
    public boolean doSetModelRotations;

    public int protocolVersion;

    public int[] lastChestCoords;

    @SubscribeEvent
    public void onTick(TickEvent event)
    {
        if (Keyboard.isKeyDown(ClickGUI.instance().toggleBind().value().key()) && MainMenu.instance().enabled() && mc.currentScreen instanceof GuiMainMenu)
        {
            ClickGUI.instance().enable();
            RoseGui.instance().openGuiMainMenu();
        } else if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE) && MainMenu.instance().enabled() && mc.currentScreen instanceof RoseGui && ClickGUI.instance().enabled())
        {
            mc.displayGuiScreen(new GuiMainMenu());
            ClickGUI.instance().disable();
        }
    }

    @SubscribeEvent
    public void onRender2d(Render2dEvent event)
    {
        Registry.modules()
                .stream()
                .filter(Module::enabled)
                .forEach(
                        m -> m.onRender2d(event.partialTicks())
                );
    }

    @SubscribeEvent
    private void onGLContextCreated(GLContextCreatedEvent event)
    {
        Registry.modules().forEach(Module::initGL);
    }

    @SubscribeEvent
    public void onInputEvent$KeyInput(InputEvent.KeyInputEvent event)
    {
        if (Keyboard.getEventKey() == Keyboard.KEY_NONE)
        {
            return;
        }

        if (Keyboard.isRepeatEvent())
        {
            return;
        }

        Registry.modules().forEach(m ->
        {
            if (m.enabled())
            {
                for (Setting<?> setting : m.settings())
                {
                    if (setting.value() instanceof Bind && !setting.equals(m.toggleBind()))
                    {
                        @SuppressWarnings("unchecked")
                        Bind bind = ((Setting<Bind>) setting).value();
                        if (bind.type() == Bind.BindType.KEYBOARD && bind.key() == Keyboard.getEventKey())
                        {
                            bind.runAction();
                        }
                    }
                }
            }

            Bind bind = m.toggleBind().value();
            if (bind.type() == Bind.BindType.KEYBOARD && bind.key() == Keyboard.getEventKey())
            {
                Setting<Module.BindMode> mode = m.bindMode();
                switch (mode.value())
                {
                    case Normal:
                        if (Keyboard.getEventKeyState())
                        {
                            bind.runAction();
                        }
                        break;
                    case Hold:
                        bind.runAction();
                        break;
                }
            }
        });
    }

    @SubscribeEvent
    public void onInputEvent$MouseInput(InputEvent.MouseInputEvent event)
    {
        Registry.modules().forEach(m ->
        {
            if (m.enabled())
            {
                for (Setting<?> setting : m.settings())
                {
                    if (setting.value() instanceof Bind && !setting.equals(m.toggleBind()))
                    {
                        @SuppressWarnings("unchecked")
                        Bind bind = ((Setting<Bind>) setting).value();
                        if (bind.type() == Bind.BindType.MOUSE && bind.key() == Mouse.getEventButton())
                        {
                            bind.runAction();
                        }
                    }
                }
            }

            Bind bind = m.toggleBind().value();
            if (bind.type() == Bind.BindType.MOUSE && bind.key() == Mouse.getEventButton())
            {
                Setting<Module.BindMode> mode = m.bindMode();
                switch (mode.value())
                {
                    case Normal:
                        if (Mouse.getEventButtonState())
                        {
                            bind.runAction();
                        }
                        break;
                    case Hold:
                        bind.runAction();
                        break;
                }
            }
        });
    }

    @SubscribeEvent
    public void onChat(ChatEvent event)
    {
        String message;
        if ((message = event.message()).startsWith(Registry.prefix()))
        {
            event.setCancelled(true);
            String[] words = message.split(" ");
            String[] args = ArrayUtils.remove(words, 0);
            Command command = null;
            for (Command cmd : Registry.commands())
            {
                if (cmd.name().equalsIgnoreCase(words[0].substring(1)))
                {
                    command = cmd;
                    break;
                }
            }
            if (command != null)
            {
                command.execute(args);
            }
            else
            {
                mc.ingameGUI.addChatMessage("Unknown command. Try .commands for a list of commands.");
            }
        }
    }

    @SubscribeEvent
    public void onPacket(PacketEvent event)
    {
        if (event.serverBound())
        {
            if (event.packet() instanceof Packet15Place packet)
            {
                if (mc.theWorld.getBlockId(packet.xPosition, packet.yPosition, packet.zPosition) == Block.chest.blockID)
                {
                    lastChestCoords = new int[]{packet.xPosition, packet.yPosition, packet.zPosition, packet.direction};
                }
                else
                {
                    lastChestCoords = null;
                }
            }
        }

        if (FabricLoader.getInstance().isModLoaded("multiproto"))
        {
            protocolVersion = MultiprotoIntegrationHelper.getProtocolVersionAsInt();
        }
        else
        {
            protocolVersion = 14;
        }
    }

    @SubscribeEvent
    private void onRenderEntity(RenderEntityEvent event)
    {
        if (event.entity().equals(mc.thePlayer) && RenderManager.instance.playerViewY != 180.0F && this.doSetModelRotations)
        {
            event.setHeadPitch(this.prevRotationRenderPitch + ((this.rotationRenderPitch - this.prevRotationRenderPitch) * event.partialTicks()));
            event.setNetHeadYaw(0);
            this.prevRotationRenderPitch = this.rotationRenderPitch;
        }
    }

    public static Factory instance()
    {
        return instance;
    }
}
