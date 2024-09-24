package me.ht9.rose.mixin.mixins;

import me.ht9.rose.Rose;
import me.ht9.rose.event.events.PosRotUpdateEvent;
import me.ht9.rose.event.factory.Factory;
import net.minecraft.src.EntityClientPlayerMP;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = EntityClientPlayerMP.class)
public class MixinEntityClientPlayerMP extends EntityPlayer
{
    @Unique
    private double oldX;
    @Unique
    private double oldY;
    @Unique
    private double oldZ;
    @Unique
    private float oldYaw;
    @Unique
    private float oldPitch;
    @Unique
    private boolean oldOnGround;

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

    @Override
    public void func_6420_o() { }
}