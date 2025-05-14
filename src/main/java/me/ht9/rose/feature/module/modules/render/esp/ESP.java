package me.ht9.rose.feature.module.modules.render.esp;

import me.ht9.rose.event.bus.annotation.SubscribeEvent;
import me.ht9.rose.event.events.RenderWorldEvent;
import me.ht9.rose.event.events.RenderWorldPassEvent;
import me.ht9.rose.feature.module.Module;
import me.ht9.rose.feature.module.annotation.Description;
import me.ht9.rose.feature.module.setting.Setting;
import me.ht9.rose.mixin.accessors.EntityRendererAccessor;
import me.ht9.rose.mixinterface.IMinecraft;
import me.ht9.rose.util.render.Render2d;
import me.ht9.rose.util.render.shader.Framebuffer;
import me.ht9.rose.util.render.shader.GlStateManager;
import net.minecraft.src.*;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

@Description("Draws entities through walls")
public final class ESP extends Module
{
    private static final ESP instance = new ESP();

    private final Setting<Boolean> rainbow = new Setting<>("Rainbow", true);
    private final Setting<Boolean> fill = new Setting<>("Fill", true);
    private final Setting<Boolean> dotted = new Setting<>("Dotted", true, fill::value);

    private final Setting<Integer> red = new Setting<>("Red", 0, 30, 255, () -> !rainbow.value());
    private final Setting<Integer> green = new Setting<>("Green", 0, 159, 255, () -> !rainbow.value());
    private final Setting<Integer> blue = new Setting<>("Blue", 0, 78, 255, () -> !rainbow.value());

    private final Setting<Boolean> all = new Setting<>("All", true);
    private final Setting<Boolean> players = new Setting<>("Players", true, () -> !all.value());
    private final Setting<Boolean> animals = new Setting<>("Animals", true, () -> !all.value());
    private final Setting<Boolean> mobs = new Setting<>("Mobs", true, () -> !all.value());
    private final Setting<Boolean> items = new Setting<>("Items", true, () -> !all.value());

    private EndShader shader;

    private int size = 0;

    private ESP()
    {
        setArrayListInfo(() -> String.valueOf(size));
    }

    @Override
    public void initGL() {
        this.shader = EndShader.instance();
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public void onRenderWorldPass(RenderWorldPassEvent event)
    {
        if (mc.thePlayer == null || mc.theWorld == null) return;

        List<Entity> entities = new ArrayList<>();
        this.size = 0;
        for (Object object : mc.theWorld.loadedEntityList)
        {
            if (!(object instanceof Entity entity)) continue;
            if (entity.equals(mc.thePlayer)) continue;

            if (
                    all.value()
                            || (entity instanceof EntityPlayer && players.value())
                            || ((entity instanceof EntityAnimal || entity instanceof EntityWaterMob) && animals.value())
                            || ((entity instanceof EntityMob || entity instanceof EntityFlying) && mobs.value())
                            || (entity instanceof EntityItem && items.value())
            )
            {
                entities.add(entity);
                this.size++;
            }
        }

        ((EntityRendererAccessor) mc.entityRenderer).invokeRenderHand(event.partialTicks(), 2);

        GlStateManager.pushMatrix();
        GlStateManager.pushAttrib();

        Framebuffer framebuffer = shader.getFramebuffer();
        framebuffer.framebufferClear();
        framebuffer.bindFramebuffer(true);

        ((EntityRendererAccessor) mc.entityRenderer).invokeSetupCameraTransform(event.partialTicks(), 0);
        for (Entity entity : entities)
        {
            if (entity.ticksExisted == 0)
            {
                entity.lastTickPosX = entity.posX;
                entity.lastTickPosY = entity.posY;
                entity.lastTickPosZ = entity.posZ;
            }

            glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            double var3 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double) event.partialTicks();
            double var5 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double) event.partialTicks();
            double var7 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double) event.partialTicks();
            float var9 = entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * event.partialTicks();
            float brightness = entity.getEntityBrightness(event.partialTicks());
            Render render = RenderManager.instance.getEntityRenderObject(entity);
            glColor3f(brightness, brightness, brightness);
            render.doRender(entity, var3 - RenderManager.renderPosX, var5 - RenderManager.renderPosY, var7 - RenderManager.renderPosZ, var9, event.partialTicks());
        }

        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        mc.entityRenderer.func_905_b();
        ((IMinecraft) mc).rose_babric$framebuffer().bindFramebuffer(true);
        glUseProgram(shader.programId());

        ScaledResolution sr = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
        glUniform2f(shader.getUniform("resolution"), (float) sr.getScaledWidth() * 2.5f, (float) sr.getScaledHeight() * 2.5f);
        glUniform1f(shader.getUniform("time"), (((System.nanoTime() / 1000000F) * 3) % 1000000) / 5000.0f);

        glUniform1f(shader.getUniform("red"), (float) red.value() / 255.0f);
        glUniform1f(shader.getUniform("green"), (float) green.value() / 255.0f);
        glUniform1f(shader.getUniform("blue"), (float) blue.value() / 255.0f);

        glUniform1i(shader.getUniform("rainbow"), rainbow.value() ? 1 : 0);
        glUniform1i(shader.getUniform("fill"), fill.value() ? 1 : 0);
        glUniform1i(shader.getUniform("dotted"), dotted.value() ? 1 : 0);

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, framebuffer.texture);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2d(0, 1);
        GL11.glVertex2d(0, 0);
        GL11.glTexCoord2d(0, 0);
        GL11.glVertex2d(0, sr.getScaledHeight());
        GL11.glTexCoord2d(1, 0);
        GL11.glVertex2d(sr.getScaledWidth(), sr.getScaledHeight());
        GL11.glTexCoord2d(1, 1);
        GL11.glVertex2d(sr.getScaledWidth(), 0);
        GL11.glEnd();
        glUseProgram(0);
        ((IMinecraft) mc).rose_babric$framebuffer().bindFramebuffer(false);

        glDisable(GL_LINE_SMOOTH);

        GlStateManager.disableBlend();

        GlStateManager.popAttrib();
        GlStateManager.popMatrix();
    }

    public static ESP instance()
    {
        return instance;
    }
}
