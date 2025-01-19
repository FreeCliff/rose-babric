package me.ht9.rose.util.render;

import me.ht9.rose.Rose;
import me.ht9.rose.feature.gui.font.CFontRenderer;
import me.ht9.rose.feature.module.modules.client.clickgui.ClickGUI;
import me.ht9.rose.feature.module.modules.client.hud.Hud;
import me.ht9.rose.mixin.accessors.IMinecraft;
import me.ht9.rose.util.Globals;
import net.minecraft.src.Tessellator;

import java.awt.*;
import java.util.Objects;

import static org.lwjgl.opengl.GL11.*;

public final class Render2d implements Globals
{
    private static CFontRenderer fontRenderer_small;
    private static CFontRenderer fontRenderer_large;

    public static void initGL()
    {
        try
        {
            Font productSans = Font.createFont(0, Objects.requireNonNull(Rose.class.getResourceAsStream("/assets/rose/fonts/ProductSans.ttf")));
            fontRenderer_small = new CFontRenderer(productSans.deriveFont(20.0f));
            fontRenderer_large = new CFontRenderer(productSans.deriveFont(24.0f));
        }
        catch (Throwable t)
        {
            Rose.logger().error("Failed to load font", t);
            throw new RuntimeException(t);
        }
    }

    public static void drawRect(float x, float y, float width, float height, Color color)
    {
        drawRectInternal(() ->
        {
            float red = color.getRed() / 255.0F;
            float green = color.getGreen() / 255.0F;
            float blue = color.getBlue() / 255.0F;
            float alpha = color.getAlpha() / 255.0F;

            Tessellator tessellator = Tessellator.instance;
            tessellator.startDrawing(GL_QUADS);
            tessellator.setColorRGBA_F(red, green, blue, alpha);
            tessellator.addVertex(x, y, 0.0F);
            tessellator.setColorRGBA_F(red, green, blue, alpha);
            tessellator.addVertex(x, y + height, 0.0F);
            tessellator.setColorRGBA_F(red, green, blue, alpha);
            tessellator.addVertex(x + width, y + height, 0.0F);
            tessellator.setColorRGBA_F(red, green, blue, alpha);
            tessellator.addVertex(x + width, y, 0.0F);

            tessellator.draw();
        });
    }

    private static void drawRectInternal(Runnable renderCode)
    {
        glPushMatrix();

        boolean blend = glIsEnabled(GL_BLEND);
        glEnable(GL_BLEND);

        boolean texture2d = glIsEnabled(GL_TEXTURE_2D);
        glDisable(GL_TEXTURE_2D);

        boolean alpha = glIsEnabled(GL_ALPHA_TEST);
        glDisable(GL_ALPHA_TEST);

        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glShadeModel(GL_SMOOTH);

        renderCode.run();

        glShadeModel(GL_FLAT);

        if (alpha)
        {
            glEnable(GL_ALPHA_TEST);
        }

        if (texture2d)
        {
            glEnable(GL_TEXTURE_2D);
        }

        if (blend)
        {
            glEnable(GL_BLEND);
        }

        glPopMatrix();
    }

    public static float renderPartialTicks()
    {
        return ((IMinecraft) mc).timer().renderPartialTicks;
    }

    public static void drawStringWithShadow(String s, float x, float y, Color color, boolean customFont)
    {
        s = ClickGUI.instance().lowerCase.value() ? s.toLowerCase() : s;
        if (customFont)
            fontRenderer_small.drawStringWithShadow(s, x, y, color.getRGB());
        else
            mc.fontRenderer.drawStringWithShadow(s, (int) x, (int) y, color.getRGB());
    }

    public static void drawGradientStringWithShadow(String s, float x, float y, boolean customFont)
    {
        s = ClickGUI.instance().lowerCase.value() ? s.toLowerCase() : s;

        if (customFont)
        {
            fontRenderer_small.drawGradientString(s, x + 1.0f, y, true);
            fontRenderer_small.drawGradientString(s, x, y - 1.0f, false);
        }
        else
        {
            int currX = (int) x;
            for (char c : s.toCharArray())
            {
                Color synced = Hud.instance().getColor(currX);
                int syncedInt = Hud.instance().getRGBA(synced);
                mc.fontRenderer.drawStringWithShadow(String.valueOf(c), currX, (int)y, syncedInt);
                currX += mc.fontRenderer.getStringWidth(String.valueOf(c));
            }
        }
    }

    public static float getStringWidth(String s, boolean customFont)
    {
        s = ClickGUI.instance().lowerCase.value() ? s.toLowerCase() : s;
        if (customFont)
            return fontRenderer_small.getStringWidth(s);
        return mc.fontRenderer.getStringWidth(s);
    }

    public static float stringWidth(String s)
    {
        return mc.fontRenderer.getStringWidth(s);
    }
}