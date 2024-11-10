package me.ht9.rose.mixin.mixins;

import net.minecraft.src.RenderList;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.nio.IntBuffer;

@Mixin(RenderList.class)
public class MixinRenderList {
    @Shadow private int field_1242_a;
    @Shadow private int field_1241_b;
    @Shadow private int field_1240_c;
    @Shadow private IntBuffer field_1236_g;

    @Unique private double d1;
    @Unique private double d2;
    @Unique private double d3;

    @Inject(method = "func_861_a", at = @At("RETURN"))
    public void setVars(int j, int k, int d, double e, double f, double par6, CallbackInfo ci) {
        d1 = e;
        d2 = f;
        d3 = par6;
    }

    @Inject(method = "func_860_a", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glTranslatef(FFF)V"), cancellable = true)
    public void fixJitter(CallbackInfo ci) {
        GL11.glTranslated((double)this.field_1242_a - this.d1, (double)this.field_1241_b - this.d2, (double)this.field_1240_c - this.d3);
        GL11.glCallLists(this.field_1236_g);
        GL11.glPopMatrix();
        ci.cancel();
    }
}
