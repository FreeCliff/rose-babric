package me.ht9.rose.mixin.mixins;

import me.ht9.rose.feature.module.modules.movement.jesus.Jesus;
import me.ht9.rose.util.Globals;
import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockFluid.class)
public class MixinBlockFluid extends Block
{
    protected MixinBlockFluid(int i, Material material)
    {
        super(i, material);
    }

    @Inject(method = "getCollisionBoundingBoxFromPool", at = @At("HEAD"), cancellable = true)
    public void getCollusionBoundingBoxFromPool$Head(World world, int i, int j, int k, CallbackInfoReturnable<AxisAlignedBB> cir)
    {
        if (Jesus.instance().enabled())
        {
            if (Jesus.instance().mode.value() == Jesus.Mode.Bob)
            {
                if ((Globals.mc.thePlayer.handleLavaMovement() || Globals.mc.thePlayer.isInWater()))
                    Globals.mc.thePlayer.motionY = 0.42;
                return;
            }

            if ((Globals.mc.thePlayer.isSneaking() || Globals.mc.thePlayer.handleLavaMovement() || Globals.mc.thePlayer.isInWater()) && Jesus.instance().mode.value() == Jesus.Mode.SemiSolid)
            {
                if (!Globals.mc.thePlayer.isSneaking())
                    Globals.mc.thePlayer.motionY = 0.42;
                return;
            }

            cir.setReturnValue(AxisAlignedBB.getBoundingBoxFromPool((double) i + this.minX, (double)j + this.minY, (double)k + this.minZ, (double) i + this.maxX, (double) j + this.maxY, (double) k + this.maxZ));
            cir.cancel();
        }
    }
}
