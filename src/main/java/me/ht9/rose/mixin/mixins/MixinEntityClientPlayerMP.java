package me.ht9.rose.mixin.mixins;

import me.ht9.rose.Rose;
import me.ht9.rose.event.events.ChatEvent;
import me.ht9.rose.event.events.PosRotUpdateEvent;
import me.ht9.rose.event.factory.Factory;
import me.ht9.rose.feature.module.modules.movement.speed.Speed;
import net.minecraft.src.EntityClientPlayerMP;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = EntityClientPlayerMP.class)
public abstract class MixinEntityClientPlayerMP extends EntityPlayer
{
    @Shadow public abstract void func_4056_N();

    @Unique private double oldX;
    @Unique private double oldY;
    @Unique private double oldZ;
    @Unique private float oldYaw;
    @Unique private float oldPitch;
    @Unique private boolean oldOnGround;

    public MixinEntityClientPlayerMP(World world)
    {
        super(world);
    }

    @Inject(
            method = "func_4056_N",
            at = @At(
                    value = "HEAD"
            )
    )
    public void onUpdate$Head(CallbackInfo ci)
    {
        this.oldX = this.posX;
        this.oldY = this.posY;
        this.oldZ = this.posZ;
        this.oldYaw = this.rotationYaw;
        this.oldPitch = this.rotationPitch;
        this.oldOnGround = this.onGround;

        PosRotUpdateEvent event = new PosRotUpdateEvent(
                this.posX,
                this.posY,
                this.posZ,
                this.rotationYaw,
                this.rotationPitch,
                this.onGround
        );

        Rose.bus().post(event);

        this.posX = event.packetX();
        this.posY = event.packetY();
        this.posZ = event.packetZ();
        this.rotationYaw = event.yaw();
        this.rotationPitch = event.pitch();
        this.onGround = event.onGround();

        if (event.clientRotation())
        {
            this.oldYaw = this.rotationYaw;
            this.oldPitch = this.rotationPitch;
        }

        Factory.instance().doSetModelRotations = event.isSetModelRotations();
    }

    @Inject(
            method = "func_4056_N",
            at = @At(
                    value = "RETURN"
            )
    )
    public void onUpdate$Return(CallbackInfo ci)
    {
        this.posX = this.oldX;
        this.posY = this.oldY;
        this.posZ = this.oldZ;
        this.rotationYaw = this.oldYaw;
        this.rotationPitch = this.oldPitch;
        this.onGround = this.oldOnGround;
    }

    @Inject(
            method = "onUpdate",
            at = @At(
                    value = "TAIL"
            )
    )
    public void onUpdate$Tick(CallbackInfo ci)
    {
        if (Speed.instance().enabled() && Speed.instance().type.value() == Speed.Type.NoCheat)
        {
            int hurtTimeBackup = this.hurtTime;
            float prevSwingProgressBackup = this.prevSwingProgress;
            float swingProgressBackup = this.swingProgress;
            int swingProgressIntBackup = this.swingProgressInt;
            float rotationYawBackup = this.rotationYaw;
            float prevRotationYawBackup = this.prevRotationYaw;
            float renderYawOffsetBackup = this.renderYawOffset;
            float prevRenderYawOffsetBackup = this.prevRenderYawOffset;
            float distanceWalkedBackup = this.distanceWalkedModified;
            float prevDistanceWalkedBackup = this.prevDistanceWalkedModified;

            for (int i = 0; i < Speed.instance().speedi.value(); ++i)
            {
                super.onUpdate();

                this.hurtTime = hurtTimeBackup;
                this.prevSwingProgress = prevSwingProgressBackup;
                this.swingProgress = swingProgressBackup;
                this.swingProgressInt = swingProgressIntBackup;
                this.rotationYaw = rotationYawBackup;
                this.prevRotationYaw = prevRotationYawBackup;
                this.renderYawOffset = renderYawOffsetBackup;
                this.prevRenderYawOffset = prevRenderYawOffsetBackup;
                this.distanceWalkedModified = distanceWalkedBackup;
                this.prevDistanceWalkedModified = prevDistanceWalkedBackup;

                this.func_4056_N();
            }
        }
    }

    @Inject(
            method = "sendChatMessage",
            at = @At("HEAD"),
            cancellable = true
    )
    public void sendChatMessage(String message, CallbackInfo ci)
    {
        ChatEvent event = new ChatEvent(message);
        Rose.bus().post(event);
        if (event.cancelled())
            ci.cancel();
    }
}