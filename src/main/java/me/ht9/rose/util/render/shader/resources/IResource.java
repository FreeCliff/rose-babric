package me.ht9.rose.util.render.shader.resources;

import me.ht9.rose.util.render.shader.resources.data.IMetadataSection;

import java.io.InputStream;

public interface IResource
{
    InputStream getInputStream();

    boolean hasMetadata();

    IMetadataSection getMetadata(String var1);
}
