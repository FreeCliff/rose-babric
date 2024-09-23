package me.ht9.rose.event.factory;

import me.ht9.rose.util.Globals;

public class Factory implements Globals
{
    private static final Factory instance = new Factory();

    public float rotationRenderPitch;
    public float prevRotationRenderPitch;
    public boolean doSetModelRotations;

    public static Factory instance()
    {
        return instance;
    }
}
