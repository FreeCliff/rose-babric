package me.ht9.rose.event.events;

import me.ht9.rose.event.Event;
import me.ht9.rose.event.factory.Factory;
import me.ht9.rose.util.Globals;

public final class PosRotUpdateEvent extends Event implements Globals
{
    private double packetX;
    private double packetY;
    private double packetZ;
    private float yaw;
    private float pitch;
    private boolean onGround;

    private boolean setModelRotations;
    private boolean clientRotation;

    public PosRotUpdateEvent(double packetX, double packetY, double packetZ, float yaw, float pitch, boolean onGround)
    {
        this.packetX = packetX;
        this.packetY = packetY;
        this.packetZ = packetZ;
        this.yaw = yaw;
        this.pitch = pitch;
        this.onGround = onGround;
    }

    public void setModelRotations()
    {
        mc.thePlayer.renderYawOffset = mc.thePlayer.renderYawOffset + ((this.yaw - mc.thePlayer.renderYawOffset) % 360 + 540) % 360 - 180;
        Factory.instance().rotationRenderPitch = this.pitch;

        this.setModelRotations = true;
    }

    public void setClientRotation(boolean clientRotation)
    {
        this.clientRotation = true;
    }

    public double packetX()
    {
        return this.packetX;
    }

    public double packetY()
    {
        return this.packetY;
    }

    public double packetZ()
    {
        return this.packetZ;
    }

    public float yaw()
    {
        return this.yaw;
    }

    public float pitch()
    {
        return this.pitch;
    }

    public boolean onGround()
    {
        return this.onGround;
    }

    public boolean isSetModelRotations()
    {
        return this.setModelRotations;
    }

    public boolean clientRotation()
    {
        return clientRotation;
    }

    public void setPacketX(double packetX)
    {
        this.packetX = packetX;
    }

    public void setPacketY(double packetY)
    {
        this.packetY = packetY;
    }

    public void setPacketZ(double packetZ)
    {
        this.packetZ = packetZ;
    }

    public void setYaw(float yaw)
    {
        this.yaw = yaw;
    }

    public void setPitch(float pitch)
    {
        this.pitch = pitch;
    }

    public void setOnGround(boolean onGround)
    {
        this.onGround = onGround;
    }
}