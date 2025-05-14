package me.ht9.rose.feature.module.modules.render.esp;

import com.google.common.io.CharStreams;
import me.ht9.rose.Rose;
import me.ht9.rose.util.Globals;
import me.ht9.rose.util.render.shader.Framebuffer;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.ARBShaderObjects.*;
import static org.lwjgl.opengl.GL20.*;

public final class EndShader implements Globals
{
    private static final EndShader instance = new EndShader();

    private final int programId;
    private final Map<String, Integer> uniformMap = new HashMap<>();
    private Framebuffer framebuffer;

    public EndShader()
    {
        try
        {
            int vertId = this.loadShader(this.readAndShut(Rose.class.getResourceAsStream("/assets/rose/shaders/vertex.vert")), GL_VERTEX_SHADER);
            int fragId = this.loadShader(this.readAndShut(Rose.class.getResourceAsStream("/assets/rose/shaders/outline.frag")), GL_FRAGMENT_SHADER);

            this.programId = glCreateProgramObjectARB();
            glAttachObjectARB(this.programId, vertId);
            glAttachObjectARB(this.programId, fragId);
            glLinkProgramARB(this.programId);
            glValidateProgramARB(this.programId);

            for (String uniform : new String[] { "resolution", "time", "red", "green", "blue", "rainbow", "fill", "dotted" })
            {
                int address = glGetUniformLocation(this.programId, uniform);
                this.uniformMap.put(uniform, address);
            }
        } catch (Throwable t)
        {
            Rose.logger().error("Failed to load end shader", t);
            throw new RuntimeException(t);
        }
    }

    private int loadShader(String src, int type)
    {
        int shader = glCreateShaderObjectARB(type);
        glShaderSourceARB(shader, src);
        glCompileShaderARB(shader);
        return shader;
    }

    @SuppressWarnings("all")
    private String readAndShut(InputStream is) throws IOException
    {
        String read = CharStreams.toString(new InputStreamReader(is, Charset.defaultCharset()));
        IOUtils.closeQuietly(is);
        return read;
    }

    public int getUniform(String name)
    {
        return this.uniformMap.get(name);
    }

    public Framebuffer getFramebuffer()
    {
        if (this.framebuffer != null) this.framebuffer.delete();
        this.framebuffer = new Framebuffer(mc.displayWidth, mc.displayHeight, true);
        return this.framebuffer;
    }

    public int programId()
    {
        return programId;
    }

    public static EndShader instance()
    {
        return instance;
    }
}
