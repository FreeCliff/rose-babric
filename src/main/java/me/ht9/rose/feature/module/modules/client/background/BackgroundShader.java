package me.ht9.rose.feature.module.modules.client.background;

import me.ht9.rose.Rose;
import me.ht9.rose.util.render.shader.ShaderProgram;

import static org.lwjgl.opengl.GL20.glUniform1f;
import static org.lwjgl.opengl.GL20.glUniform2f;

public class BackgroundShader extends ShaderProgram
{
    public BackgroundShader(String fragment)
    {
        super("/assets/rose/shaders/vertex.vert", fragment);
    }

    public void updateUniforms()
    {
        glUniform2f(getUniform("resolution"), mc.displayWidth, mc.displayHeight);
        glUniform1f(getUniform("time"), (System.nanoTime() - Rose.startTime()) / 1000000000f);
    }
}
