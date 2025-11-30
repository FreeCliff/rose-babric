package me.ht9.rose.feature.module.modules.misc.autocaptcha;

import me.ht9.rose.event.bus.annotation.SubscribeEvent;
import me.ht9.rose.event.events.PacketEvent;
import me.ht9.rose.feature.module.Module;
import me.ht9.rose.feature.module.annotation.Description;
import net.minecraft.src.Packet3Chat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Description("Automatically does Aleksandar Haralanov ChatGuard captchas for you.")
public final class AutoCaptcha extends Module
{
    private static final AutoCaptcha instance = new AutoCaptcha();

    // thanks aleksander for making it impossible for server owners to change this without compiling the plugin themselves <3
    private static final Pattern CAPTCHA_PATTERN = Pattern.compile("^§cUse §e/cg captcha §b(.[^ ]+) §cto verify\\. Case-sensitive!$");

    @SubscribeEvent
    public void onPacket(PacketEvent event)
    {
        if (event.serverBound()) return;
        if (!(event.packet() instanceof Packet3Chat packet)) return;

        Matcher m = CAPTCHA_PATTERN.matcher(packet.message);
        if (m.matches())
        {
            String code = m.group(1);
            mc.getSendQueue().addToSendQueue(new Packet3Chat("/cg captcha " + code));
            mc.ingameGUI.addChatMessage("Automatically did captcha: " + code);
        }
    }

    public static AutoCaptcha instance()
    {
        return instance;
    }
}
