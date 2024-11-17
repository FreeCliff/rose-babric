package me.ht9.rose.feature.module.modules.render.nametags;

import me.ht9.rose.event.bus.annotation.SubscribeEvent;
import me.ht9.rose.event.events.RenderPlayerLabelEvent;
import me.ht9.rose.feature.module.Module;
import net.minecraft.src.RenderManager;
import net.minecraft.src.Tessellator;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

public final class NameTags extends Module
{
    private static final NameTags instance = new NameTags();

    @SuppressWarnings("unused")
    @SubscribeEvent
    public void onLabelRender(RenderPlayerLabelEvent event)
    {
        float distance = event.entity().getDistanceToEntity(RenderManager.instance.livingPlayer);
        Color color = Color.WHITE;
        if (distance > (mc.thePlayer.isSneaking() ? 32f : 64f))
            color = Color.GREEN;
        if (event.entity().isSneaking())
            color = Color.RED;

        double y = event.y();
        if (event.entity().isPlayerSleeping())
            y -= 1.5;

        float scale = Math.max(0.016666668f * 1.6f, 0.016666668f * distance * 0.115f);

        glPushMatrix();
        glTranslated(event.x(), y + 2.3, event.z());
        glNormal3f(0.0f, 1.0f, 0.0f);
        glRotatef(-RenderManager.instance.playerViewY, 0.0f, 1.0f, 0.0f);
        glRotatef(RenderManager.instance.playerViewX, 1.0f, 0.0f, 0.0f);
        glScalef(-scale, -scale, scale);
        glDisable(GL_LIGHTING);
        glDepthMask(false);
        glDisable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        Tessellator tessellator = Tessellator.instance;

        glDisable(GL_TEXTURE_2D);
        tessellator.startDrawingQuads();
        int center = mc.fontRenderer.getStringWidth(event.name()) / 2;
        tessellator.setColorRGBA_F(0f, 0f, 0f, .25f);
        tessellator.addVertex(-center - 1, -1, 0.0);
        tessellator.addVertex(-center - 1, 8, 0.0);
        tessellator.addVertex(center + 1, 8, 0.0);
        tessellator.addVertex(center + 1, -1, 0.0);
        tessellator.draw();
        glEnable(GL_TEXTURE_2D);
        mc.fontRenderer.drawString(event.name(), -center, 0, color.getRGB());
        glEnable(GL_DEPTH_TEST);
        glDepthMask(true);
        glEnable(GL_LIGHTING);
        glDisable(GL_BLEND);
        glColor4f(1f, 1f, 1f, 1f);
        glPopMatrix();

        event.setCancelled(true);
    }

    public static NameTags instance()
    {
        return instance;
    }
}
