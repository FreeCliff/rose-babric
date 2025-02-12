package me.ht9.rose.mixin.mixins;

import me.ht9.rose.feature.module.modules.client.background.Background;
import me.ht9.rose.feature.module.modules.client.hud.Hud;
import me.ht9.rose.util.render.Shader;
import net.minecraft.client.Minecraft;
import net.minecraft.src.Gui;
import net.minecraft.src.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

@Mixin(value = GuiScreen.class)
public class MixinGuiScreen extends Gui
{
    @Shadow protected Minecraft mc;

    @Redirect(method = "drawWorldBackground", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/GuiScreen;drawGradientRect(IIIIII)V"))
    public void drawWorldBackground$drawGradientRect(GuiScreen instance, int x, int y, int x2, int y2, int topCol, int bottomCol)
    {
        if (Background.instance().enabled() && Background.instance().customGradient.value())
        {
            topCol = Hud.instance().getRGBA(
                    Background.instance().topRed.value(),
                    Background.instance().topGreen.value(),
                    Background.instance().topBlue.value(),
                    Background.instance().topAlpha.value()
            );
            bottomCol = Hud.instance().getRGBA(
                    Background.instance().bottomRed.value(),
                    Background.instance().bottomGreen.value(),
                    Background.instance().bottomBlue.value(),
                    Background.instance().bottomAlpha.value()
            );
        }

        this.drawGradientRect(x, y, x2, y2, topCol, bottomCol);
    }

    @Inject(
            method = "drawBackground",
            at = @At(
                    value = "HEAD"
            )
    )
    public void drawBackground$Head(int par1, CallbackInfo ci)
    {
        if (Background.instance().enabled() && Background.instance().shader.value())
        {
            glPushMatrix();
            glPushAttrib(GL_ENABLE_BIT | GL_LIGHTING_BIT);

            Shader shader = Background.instance().shader();
            glUseProgram(shader.programId());
            Background.instance().setupUniforms();
        }
    }

    @Inject(
            method = "drawBackground",
            at = @At(
                    value = "RETURN"
            )
    )
    public void drawBackground$Return(int par1, CallbackInfo ci)
    {
        if (Background.instance().enabled() && Background.instance().shader.value())
        {
            glUseProgram(0);
            glPopAttrib();
            glPopMatrix();
        }
    }
}