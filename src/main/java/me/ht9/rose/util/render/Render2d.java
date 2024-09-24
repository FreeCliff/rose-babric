package me.ht9.rose.util.render;

import me.ht9.rose.mixin.accessors.IMinecraft;
import me.ht9.rose.util.Globals;
import net.minecraft.src.Tessellator;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public final class Render2d implements Globals
{
    public static void drawRect(float x, float y, float width, float height, Color color)
    {
        drawRectInternal(() ->
        {
            float red = color.getRed() / 255.0F;
            float green = color.getGreen() / 255.0F;
            float blue = color.getBlue() / 255.0F;
            float alpha = color.getAlpha() / 255.0F;

            Tessellator tessellator = Tessellator.instance;
            tessellator.startDrawing(GL11.GL_QUADS);
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
        GL11.glPushMatrix();

        boolean blend = GL11.glIsEnabled(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_BLEND);

        boolean texture2d = GL11.glIsEnabled(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_TEXTURE_2D);

        boolean alpha = GL11.glIsEnabled(GL11.GL_ALPHA_TEST);
        GL11.glDisable(GL11.GL_ALPHA_TEST);

        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glShadeModel(GL11.GL_SMOOTH);

        renderCode.run();

        GL11.glShadeModel(GL11.GL_FLAT);

        if (alpha)
        {
            GL11.glEnable(GL11.GL_ALPHA_TEST);
        }

        if (texture2d)
        {
            GL11.glEnable(GL11.GL_TEXTURE_2D);
        }

        if (blend)
        {
            GL11.glEnable(GL11.GL_BLEND);
        }

        GL11.glPopMatrix();
    }

    public static float renderPartialTicks()
    {
        return ((IMinecraft) mc).timer().renderPartialTicks;
    }

    public static void drawStringWithShadow(String s, float x, float y, Color color)
    {
        mc.fontRenderer.drawStringWithShadow(s, (int) x, (int) y, (color.getAlpha() << 24) | ((color.getRed() & 255) << 16) | ((color.getGreen() & 255) << 8) | (color.getBlue() & 255));
    }

    public static float stringWidth(String s)
    {
        return mc.fontRenderer.getStringWidth(s);
    }
}