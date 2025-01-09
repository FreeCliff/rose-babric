package me.ht9.rose.feature.module.modules.client.hud;

import me.ht9.rose.feature.module.Module;
import me.ht9.rose.feature.module.annotation.Description;
import me.ht9.rose.feature.registry.Registry;
import me.ht9.rose.util.misc.FontColor;
import net.minecraft.src.ScaledResolution;

import java.awt.*;
import java.util.List;

@Description("Cool")
public final class Hud extends Module
{
    private static final Hud instance = new Hud();

    @Override
    public void onRender2d(float partialTicks)
    {
        ScaledResolution sr = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
        List<Module> modules = Registry.modules().stream().filter(Module::enabled).sorted((a, b) -> Integer.compare(mc.fontRenderer.getStringWidth(b.name()), mc.fontRenderer.getStringWidth(a.name()))).toList();

        drawRainbowString("rose", 4, 4);

        int y = 4;
        for (Module module : modules)
        {
            String arraylistInfo = module.arraylistInfo();
            String text = module.name() + (arraylistInfo.isEmpty() ? "" : FontColor.GRAY + "[" + arraylistInfo + "]");
            mc.fontRenderer.drawStringWithShadow(text, sr.getScaledWidth() - mc.fontRenderer.getStringWidth(text) - 4, y, getRainbow(y));
            y += 10;
        }
    }

    private void drawRainbowString(String text, int x, int y)
    {
        for (char c : text.toCharArray())
        {
            mc.fontRenderer.drawStringWithShadow(String.valueOf(c), x, y, getRainbow(x));
            x += mc.fontRenderer.getStringWidth(String.valueOf(c));
        }
    }

    private int getRainbow(int offset)
    {
        return Color.HSBtoRGB((System.nanoTime() + offset * 6000000L) / 7500000000f % 1, .5f, 1f);
    }

    public static Hud instance()
    {
        return instance;
    }
}
