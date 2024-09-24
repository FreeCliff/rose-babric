package me.ht9.rose.util.module;

import me.ht9.rose.util.Globals;

public final class Movement implements Globals
{
    public static void setSpeed(double moveSpeed, float yaw, double strafe, double forward)
    {
        if (forward != 0.0D)
        {
            if (strafe > 0.0D)
            {
                yaw += ((forward > 0.0D) ? -45 : 45);
            } else if (strafe < 0.0D)
            {
                yaw += ((forward > 0.0D) ? 45 : -45);
            }
            strafe = 0.0D;
            if (forward > 0.0D)
            {
                forward = 1.0D;
            } else if (forward < 0.0D)
            {
                forward = -1.0D;
            }
        }
        if (strafe > 0.0D)
        {
            strafe = 1.0D;
        } else if (strafe < 0.0D)
        {
            strafe = -1.0D;
        }
        double mx = Math.cos(Math.toRadians((yaw + 90.0F)));
        double mz = Math.sin(Math.toRadians((yaw + 90.0F)));
        mc.thePlayer.motionX = forward * moveSpeed * mx + strafe * moveSpeed * mz;
        mc.thePlayer.motionZ = forward * moveSpeed * mz - strafe * moveSpeed * mx;
    }

    public static void setSpeed(double moveSpeed)
    {
        setSpeed(moveSpeed, mc.thePlayer.rotationYaw, mc.thePlayer.movementInput.moveStrafe, mc.thePlayer.movementInput.moveForward);
    }

    public static double getAbsoluteSpeed(double motionX, double motionZ)
    {
        return Math.sqrt(Math.pow(motionX, 2) + Math.pow(motionZ, 2));
    }
}