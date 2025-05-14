package me.ht9.rose.util.render.shader.renderer;

import me.ht9.rose.util.render.shader.resources.IResourceManager;

import java.io.IOException;

public interface ITextureObject
{
    void loadTexture(IResourceManager var1) throws IOException;

    int getGlTextureId();
}
