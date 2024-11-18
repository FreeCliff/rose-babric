package me.ht9.rose.util.render;

import me.ht9.rose.Rose;
import me.ht9.rose.util.Globals;
import net.minecraft.src.ScaledResolution;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.lwjgl.opengl.ARBShaderObjects.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

public final class Shader implements Globals
{
    private final int programId;
    private final Map<String, Integer> uniformMap = new HashMap<>();
    private Framebuffer framebuffer;

    public Shader(String vertexPath, String fragmentPath, String... uniforms)
    {
        try
        {
            int vertexId = this.createShaderObject(this.readResource(vertexPath), GL_VERTEX_SHADER);
            int fragmentId = this.createShaderObject(this.readResource(fragmentPath), GL_FRAGMENT_SHADER);

            this.programId = glCreateProgramObjectARB();
            glAttachObjectARB(this.programId, vertexId);
            glAttachObjectARB(this.programId, fragmentId);
            glLinkProgramARB(this.programId);
            glValidateProgramARB(this.programId);

            for (String uniform : uniforms)
            {
                this.uniformMap.put(uniform, glGetUniformLocation(this.programId, uniform));
            }
        }
        catch (Throwable t)
        {
            Rose.logger().error("Failed to load shader: ", t);
            throw new RuntimeException(t);
        }
    }

    private int createShaderObject(String source, int type)
    {
        int object = glCreateShaderObjectARB(type);
        glShaderSourceARB(object, source);
        glCompileShaderARB(object);
        return object;
    }

    private String readResource(String resourceName) throws IOException
    {
        Reader inputStreamReader = new InputStreamReader(Objects.requireNonNull(this.getClass().getResourceAsStream(resourceName)));
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        StringBuilder builder = new StringBuilder();
        String line;

        while ((line = bufferedReader.readLine()) != null)
        {
            builder.append(line);
        }

        inputStreamReader.close();
        bufferedReader.close();

        return builder.toString();
    }

    public int getUniform(String name)
    {
        return this.uniformMap.get(name);
    }

    public Framebuffer getFramebuffer() {
        if (this.framebuffer != null)
        {
            this.framebuffer.delete();
        }
        this.framebuffer = new Framebuffer(mc.displayWidth, mc.displayHeight);
        return this.framebuffer;
    }

    public static void drawFramebuffer(Framebuffer framebuffer) {
        ScaledResolution scaledResolution = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
        glBindTexture(GL_TEXTURE_2D, framebuffer.texture);
        glBegin(GL_QUADS);
        glTexCoord2d(0, 1);
        glVertex2d(0, 0);
        glTexCoord2d(0, 0);
        glVertex2d(0, scaledResolution.getScaledHeight());
        glTexCoord2d(1, 0);
        glVertex2d(scaledResolution.getScaledWidth(), scaledResolution.getScaledHeight());
        glTexCoord2d(1, 1);
        glVertex2d(scaledResolution.getScaledWidth(), 0);
        glEnd();
    }

    public int programId() {
        return programId;
    }
}
