package me.ht9.rose.util.render;

import net.minecraft.src.Tessellator;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.EXTFramebufferObject.*;
import static org.lwjgl.opengl.GL11.*;

public final class Framebuffer
{
    public static Framebuffer framebuffer;

    public int width;
    public int height;

    private int fbo;
    public int texture;
    private int depthbuffer;
    private final float[] color;

    public Framebuffer(int width, int height)
    {
        this.fbo = -1;
        this.texture = -1;
        this.depthbuffer = -1;
        this.color = new float[]{0f, 0f, 0f, 0f};
        createBindFramebuffer(width, height);
    }

    public void createBindFramebuffer(int width, int height)
    {
        glEnable(GL_DEPTH_TEST);

        if (fbo > -1)
            delete();

        createFramebuffer(width, height);
        checkFramebufferComplete();
        glBindFramebufferEXT(0x8D40, 0);
    }

    public void delete()
    {
        unbindFramebufferTexture();
        unbindFramebuffer();

        if (depthbuffer > -1)
        {
            glDeleteRenderbuffersEXT(depthbuffer);
            depthbuffer = -1;
        }

        if (texture > -1)
        {
            glDeleteTextures(texture);
            texture = -1;
        }

        if (fbo > -1)
        {
            glBindFramebufferEXT(0x8D40, 0);
            glDeleteFramebuffersEXT(fbo);
            fbo = -1;
        }
    }

    private void createFramebuffer(int width, int height)
    {
        this.width = width;
        this.height = height;

        fbo = glGenFramebuffersEXT();
        texture = glGenTextures();
        depthbuffer = glGenRenderbuffersEXT();

        glBindTexture(GL_TEXTURE_2D, texture);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, (ByteBuffer) null);
        glBindFramebufferEXT(0x8D40, fbo);
        glFramebufferTexture2DEXT(0x8D40, 0x8CE0, 0xDE1, texture, 0);

        glBindRenderbufferEXT(0x8D41, depthbuffer);
        glRenderbufferStorageEXT(0x8D41, 0x81A6, width, height);
        glFramebufferRenderbufferEXT(0x8D40, 0x8D00, 0x8D41, depthbuffer);

        clearFramebuffer();
        unbindFramebufferTexture();
    }

    private void bindFramebufferTexture()
    {
        glBindTexture(GL_TEXTURE_2D, texture);
    }

    private void unbindFramebufferTexture()
    {
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public void bindFramebuffer(boolean setViewport)
    {
        glBindFramebufferEXT(0x8D40, fbo);

        if (setViewport)
            glViewport(0, 0, width, height);
    }

    public void unbindFramebuffer()
    {
        glBindFramebufferEXT(0x8D40, 0);
    }

    private void checkFramebufferComplete()
    {
        int status = glCheckFramebufferStatusEXT(0x8D40);

        if (status != 0x8CD5)
            throw new RuntimeException("glCheckFramebufferStatusEXT returned status code " + status);
    }

    public void setFramebufferColor(float r, float g, float b, float a)
    {
        this.color[0] = r;
        this.color[1] = g;
        this.color[2] = b;
        this.color[3] = a;
    }

    public void renderFramebuffer(int width, int height)
    {
        glColorMask(true, true, true, false);
        glDisable(GL_DEPTH_TEST);
        glDepthMask(false);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0.0, width, height, 0.0, 1000.0, 3000.0);
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
        glTranslatef(0f, 0f, -2000f);
        glViewport(0, 0, width, height);
        glEnable(GL_TEXTURE_2D);
        glDisable(GL_LIGHTING);
        glDisable(GL_ALPHA_TEST);
        glDisable(GL_BLEND);
        glColor4f(1f, 1f, 1f, 1f);
        glEnable(GL_COLOR_MATERIAL);
        bindFramebufferTexture();

        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(0.0d, height, 0.0d, 0.0d, 0.0d);
        tessellator.addVertexWithUV(width, height, 0.0d, 1.0d, 0.0d);
        tessellator.addVertexWithUV(width, 0.0d, 0.0d, 1.0d, 1.0d);
        tessellator.addVertexWithUV(0.0d, 0.0d, 0.0d, 0.0d, 1.0d);
        tessellator.draw();

        unbindFramebufferTexture();
        glDepthMask(true);
        glColorMask(true, true, true, true);
    }

    public void clearFramebuffer()
    {
        bindFramebuffer(true);
        glClearColor(color[0], color[1], color[2], color[3]);

        glClearDepth(1);

        glClear(0x4200);
        unbindFramebuffer();
    }
}
