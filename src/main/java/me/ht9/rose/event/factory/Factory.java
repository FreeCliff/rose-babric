package me.ht9.rose.event.factory;

import me.ht9.rose.event.bus.annotation.SubscribeEvent;
import me.ht9.rose.event.events.InputEvent;
import me.ht9.rose.event.events.PacketEvent;
import me.ht9.rose.event.events.Render2dEvent;
import me.ht9.rose.feature.command.Command;
import me.ht9.rose.feature.module.keybinding.Bind;
import me.ht9.rose.feature.module.setting.Setting;
import me.ht9.rose.feature.registry.Registry;
import me.ht9.rose.feature.module.Module;
import me.ht9.rose.util.Globals;
import net.minecraft.src.Packet3Chat;
import org.apache.commons.lang3.ArrayUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class Factory implements Globals
{
    private static final Factory instance = new Factory();

    public float rotationRenderPitch;
    public float prevRotationRenderPitch;
    public boolean doSetModelRotations;

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
    public void onPacket(PacketEvent event)
    {
        if (event.serverBound() && event.packet() instanceof Packet3Chat packet)
        {
            String message;
            if ((message = packet.message).startsWith(Registry.prefix()))
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
                } else
                {
                    mc.ingameGUI.addChatMessage("Unknown command. Try .commands for a list of commands.");
                }
            }
        }
    }

    public static Factory instance()
    {
        return instance;
    }
}
