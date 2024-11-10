package me.ht9.rose.event.events;

import me.ht9.rose.event.Event;
import net.minecraft.src.Entity;
import net.minecraft.src.ModelBase;

public final class RenderEntityEvent extends Event
{
    private final ModelBase modelBase;
    private final Entity entity;
    private float limbSwing;
    private float limbSwingAmount;
    private float ageInTicks;
    private float netHeadYaw;
    private float headPitch;
    private float scale;
    private final Stage stage;
    private final float partialTicks;

    public RenderEntityEvent(ModelBase modelBase,
                             Entity entityPlayer,
                             float limbSwing,
                             float limbSwingAmount,
                             float ageInTicks,
                             float netHeadYaw,
                             float headPitch,
                             float scale,
                             Stage stage,
                             float partialTicks)
    {
        this.modelBase = modelBase;
        this.entity = entityPlayer;
        this.limbSwing = limbSwing;
        this.limbSwingAmount = limbSwingAmount;
        this.ageInTicks = ageInTicks;
        this.netHeadYaw = netHeadYaw;
        this.headPitch = headPitch;
        this.scale = scale;
        this.stage = stage;
        this.partialTicks = partialTicks;
    }

    public ModelBase modelBase()
    {
        return this.modelBase;
    }

    public Entity entity()
    {
        return this.entity;
    }

    public float limbSwing()
    {
        return this.limbSwing;
    }

    public float limbSwingAmount()
    {
        return this.limbSwingAmount;
    }

    public float ageInTicks()
    {
        return this.ageInTicks;
    }

    public float netHeadYaw()
    {
        return this.netHeadYaw;
    }

    public float headPitch()
    {
        return this.headPitch;
    }

    public float scale()
    {
        return this.scale;
    }

    public Stage stage()
    {
        return this.stage;
    }

    public float partialTicks()
    {
        return this.partialTicks;
    }

    public void setLimbSwing(float limbSwing)
    {
        this.limbSwing = limbSwing;
    }

    public void setLimbSwingAmount(float limbSwingAmount)
    {
        this.limbSwingAmount = limbSwingAmount;
    }

    public void setAgeInTicks(float ageInTicks)
    {
        this.ageInTicks = ageInTicks;
    }

    public void setNetHeadYaw(float netHeadYaw)
    {
        this.netHeadYaw = netHeadYaw;
    }

    public void setHeadPitch(float headPitch)
    {
        this.headPitch = headPitch;
    }

    public void setScale(float scale)
    {
        this.scale = scale;
    }

    public enum Stage
    {
        PRE,
        POST
    }
}