package me.ht9.rose.feature.module.modules.client.background;

import me.ht9.rose.Rose;
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

    public final Setting<Boolean> shader = new Setting<>("UseShader", true);
    public final Setting<ShaderOption> shaderOption = new Setting<>("Shader", ShaderOption.Auroras, this.shader::value)
            .withOnChange(value ->
            {
                if (glContextCreated)
                    initGL();
            });

    public final Setting<Boolean> customGradient = new Setting<>("CustomGradient", false);
    public final Setting<Integer> topRed = new Setting<>("TopRed", 0, 16, 255, this.customGradient::value);
    public final Setting<Integer> topGreen = new Setting<>("TopGreen", 0, 16, 255, this.customGradient::value);
    public final Setting<Integer> topBlue = new Setting<>("TopBlue", 0, 16, 255, this.customGradient::value);
    public final Setting<Integer> topAlpha = new Setting<>("TopAlpha", 0, 192, 255, this.customGradient::value);
    public final Setting<Integer> bottomRed = new Setting<>("BottomRed", 0, 16, 255, this.customGradient::value);
    public final Setting<Integer> bottomGreen = new Setting<>("BottomGreen", 0, 16, 255, this.customGradient::value);
    public final Setting<Integer> bottomBlue = new Setting<>("BottomBlue", 0, 16, 255, this.customGradient::value);
    public final Setting<Integer> bottomAlpha = new Setting<>("BottomAlpha", 0, 208, 255, this.customGradient::value);

    private Shader shaderObj;

    private Background()
    {
        setArrayListInfo(() -> shaderOption.value().toString());
    }

    @Override
    public void initGL() {
        glContextCreated = true;
        shaderObj = new Shader(
                "/assets/rose/shaders/vertex.vert",
                shaderOption.value().fragmentPath,
                "resolution", "time"
        );
    }

    public void setupUniforms()
    {
        glUniform2f(shaderObj.uniform("resolution"), mc.displayWidth, mc.displayHeight);
        glUniform1f(shaderObj.uniform("time"), (System.nanoTime() - Rose.startTime()) / 1000000000.0F);
    }

    public Shader shader() {
        return shaderObj;
    }

    public static Background instance()
    {
        return instance;
    }

    public enum ShaderOption
    {
        Auroras("/assets/rose/shaders/auroras.frag"),
        CyberFuji("/assets/rose/shaders/cyberfuji.frag");

        final String fragmentPath;

        ShaderOption(String fragmentPath)
        {
            this.fragmentPath = fragmentPath;
        }
    }
}
