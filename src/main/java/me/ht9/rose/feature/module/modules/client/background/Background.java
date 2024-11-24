package me.ht9.rose.feature.module.modules.client.background;

import me.ht9.rose.feature.module.Module;
import me.ht9.rose.feature.module.annotation.Description;
import me.ht9.rose.feature.module.setting.Setting;
import me.ht9.rose.util.render.Shader;

import static org.lwjgl.opengl.GL20.*;

@Description("Draws a cool shader in menu backgrounds")
public final class Background extends Module
{
    private static final Background instance = new Background();

    private boolean glContextCreated = false;

    private final Setting<ShaderOption> shaderOption = new Setting<>("Shader", ShaderOption.Auroras)
            .withOnChange(value ->
            {
                if (glContextCreated)
                    initGL();
            });

    private Shader shader;

    @Override
    public void initGL() {
        glContextCreated = true;
        shader = new Shader(
                "/assets/rose/shaders/vertex.vert",
                shaderOption.value().fragmentPath,
                "resolution", "time"
        );
    }

    public void setupUniforms()
    {
        glUniform2f(shader.uniform("resolution"), mc.displayWidth, mc.displayHeight);
        glUniform1f(shader.uniform("time"), System.currentTimeMillis() % 120000 / 1000.0f);
    }

    public Shader shader() {
        return shader;
    }

    public static Background instance()
    {
        return instance;
    }

    public enum ShaderOption
    {
        Auroras("/assets/rose/shaders/auroras.frag");

        final String fragmentPath;

        ShaderOption(String fragmentPath)
        {
            this.fragmentPath = fragmentPath;
        }
    }
}
