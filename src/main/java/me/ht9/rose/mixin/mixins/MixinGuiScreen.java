package me.ht9.rose.mixin.mixins;

import me.ht9.rose.feature.module.modules.client.background.Background;
import me.ht9.rose.util.render.Shader;
import net.minecraft.client.Minecraft;
import net.minecraft.src.Gui;
import net.minecraft.src.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

@Mixin(value = GuiScreen.class)
public class MixinGuiScreen extends Gui
{
    @Shadow protected Minecraft mc;

    @Inject(
            method = "drawBackground",
            at = @At(
                    value = "HEAD"
            )
    )
    public void drawBackground$Head(int par1, CallbackInfo ci)
    {
        if (!Background.instance().enabled())
            return;
        glPushMatrix();
        glPushAttrib(8256);

        Shader shader = Background.instance().shader();
        glUseProgram(shader.programId());
        Background.instance().setupUniforms();
    }

    @Inject(
            method = "drawBackground",
            at = @At(
                    value = "RETURN"
            )
    )
    public void drawBackground$Return(int par1, CallbackInfo ci)
    {
        if (!Background.instance().enabled())
            return;
        glUseProgram(0);
        glPopAttrib();
        glPopMatrix();
    }
}