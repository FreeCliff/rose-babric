package me.ht9.rose.feature.module.modules.client.background;

import me.ht9.rose.feature.module.Module;
import me.ht9.rose.feature.module.annotation.Description;
import me.ht9.rose.feature.module.setting.Setting;

@Description("Draws a cool shader in menu backgrounds")
public final class Background extends Module
{
    private static final Background instance = new Background();

    public final Setting<Boolean> shader = new Setting<>("UseShader", true);
    public final Setting<ShaderOption> shaderOption = new Setting<>("Shader", ShaderOption.Auroras, this.shader::value);

    public final Setting<Boolean> customGradient = new Setting<>("CustomGradient", false);
    public final Setting<Integer> topRed = new Setting<>("TopRed", 0, 16, 255, this.customGradient::value);
    public final Setting<Integer> topGreen = new Setting<>("TopGreen", 0, 16, 255, this.customGradient::value);
    public final Setting<Integer> topBlue = new Setting<>("TopBlue", 0, 16, 255, this.customGradient::value);
    public final Setting<Integer> topAlpha = new Setting<>("TopAlpha", 0, 192, 255, this.customGradient::value);
    public final Setting<Integer> bottomRed = new Setting<>("BottomRed", 0, 16, 255, this.customGradient::value);
    public final Setting<Integer> bottomGreen = new Setting<>("BottomGreen", 0, 16, 255, this.customGradient::value);
    public final Setting<Integer> bottomBlue = new Setting<>("BottomBlue", 0, 16, 255, this.customGradient::value);
    public final Setting<Integer> bottomAlpha = new Setting<>("BottomAlpha", 0, 208, 255, this.customGradient::value);

    public AurorasShader aurorasShader;
    public CyberFujiShader cyberFujiShader;

    private Background()
    {
        setArrayListInfo(() -> shader.value() ? shaderOption.value().name() : "");
    }

    @Override
    public void initGL() {
        this.aurorasShader = new AurorasShader();
        this.cyberFujiShader = new CyberFujiShader();
    }

    public static Background instance()
    {
        return instance;
    }

    @SuppressWarnings("unused")
    public enum ShaderOption
    {
        Auroras,
        CyberFuji
    }
}
