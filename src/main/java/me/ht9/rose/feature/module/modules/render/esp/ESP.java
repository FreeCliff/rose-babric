package me.ht9.rose.feature.module.modules.render.esp;

import me.ht9.rose.event.bus.annotation.SubscribeEvent;
import me.ht9.rose.event.events.RenderWorldPassEvent;
import me.ht9.rose.feature.module.Module;
import me.ht9.rose.feature.module.annotation.Description;
import me.ht9.rose.feature.module.setting.Setting;
import me.ht9.rose.mixin.accessors.IEntityRenderer;
import me.ht9.rose.util.render.Framebuffer;
import me.ht9.rose.util.render.Shader;
import net.minecraft.src.*;

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
    private final Setting<Boolean> players = new Setting<>("Players", true);
    private final Setting<Boolean> animals = new Setting<>("Animals", true);
    private final Setting<Boolean> mobs = new Setting<>("Mobs", true);
    private final Setting<Boolean> items = new Setting<>("Items", true);

    private Shader shader;

    @Override
    public void initGL() {
        shader = new Shader(
                "/assets/rose/shaders/vertex.vert",
                "/assets/rose/shaders/outline.frag",
                "resolution", "time", "red", "green", "blue", "rainbow", "fill", "dotted"
        );
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public void onRenderWorldPass(RenderWorldPassEvent event)
    {
        if (mc.thePlayer == null || mc.theWorld == null) return;

        ((IEntityRenderer) mc.entityRenderer).invokeRenderHand(event.partialTicks(), 2);

        glPushMatrix();
        glPushAttrib(0x2040);

        Framebuffer framebuffer = shader.getFramebuffer();
        framebuffer.clearFramebuffer();
        framebuffer.bindFramebuffer(true);

        ((IEntityRenderer) mc.entityRenderer).invokeSetupCameraTransform(event.partialTicks(), 0);
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
                if (entity.ticksExisted == 0)
                {
                    entity.lastTickPosX = entity.posX;
                    entity.lastTickPosY = entity.posY;
                    entity.lastTickPosZ = entity.posZ;
                }

                glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                RenderManager.instance.renderEntity(entity, event.partialTicks());
            }
        }

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        mc.entityRenderer.func_905_b();
        Framebuffer.framebuffer.bindFramebuffer(true);
        glUseProgram(shader.programId());

        ScaledResolution sr = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
        glUniform2f(shader.getUniform("resolution"), (float) sr.getScaledWidth() * 2.5f, (float) sr.getScaledHeight() * 2.5f);
        glUniform1f(shader.getUniform("time"), ((System.currentTimeMillis() * 3) % 1000000) / 5000.0f);

        glUniform1f(shader.getUniform("red"), (float) red.value() / 255.0f);
        glUniform1f(shader.getUniform("green"), (float) green.value() / 255.0f);
        glUniform1f(shader.getUniform("blue"), (float) blue.value() / 255.0f);

        glUniform1i(shader.getUniform("rainbow"), rainbow.value() ? 1 : 0);
        glUniform1i(shader.getUniform("fill"), fill.value() ? 1 : 0);
        glUniform1i(shader.getUniform("dotted"), dotted.value() ? 1 : 0);

        Shader.drawFramebuffer(framebuffer);
        glUseProgram(0);
        Framebuffer.framebuffer.bindFramebuffer(false);

        glDisable(GL_LINE_SMOOTH);

        glDisable(GL_BLEND);

        glPopAttrib();
        glPopMatrix();
    }

    public static ESP instance()
    {
        return instance;
    }
}
