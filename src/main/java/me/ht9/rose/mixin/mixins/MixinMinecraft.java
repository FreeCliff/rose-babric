package me.ht9.rose.mixin.mixins;

import me.ht9.rose.Rose;
import me.ht9.rose.event.events.*;
import me.ht9.rose.util.render.Framebuffer;
import net.minecraft.client.Minecraft;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.World;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static org.lwjgl.opengl.GL11.*;

@Mixin(value = Minecraft.class)
public class MixinMinecraft
{
    @Shadow public MovingObjectPosition objectMouseOver;
    @Shadow public int displayWidth;
    @Shadow public int displayHeight;

    @Inject(
            method = "startGame",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/Minecraft;getMinecraftDir()Ljava/io/File;"
            )
    )
    public void glContextCreated(CallbackInfo ci)
    {
        Framebuffer.framebuffer = new Framebuffer(displayWidth, displayHeight);
        Framebuffer.framebuffer.setFramebufferColor(0.0f, 0.0f, 0.0f, 0.0f);

        Rose.bus().post(new GLContextCreatedEvent());
    }

    @Inject(
            method = "run",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/Minecraft;checkGLError(Ljava/lang/String;)V",
                    ordinal = 0
            )
    )
    public void bindFramebuffer(CallbackInfo ci)
    {
        Framebuffer.framebuffer.bindFramebuffer(true);
        glEnable(GL_ALPHA_TEST);
        glDisable(GL_DEPTH_TEST);
    }

    @Inject(
            method = "run",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/Minecraft;checkGLError(Ljava/lang/String;)V",
                    ordinal = 1
            )
    )
    public void renderFramebuffer(CallbackInfo ci)
    {
        Framebuffer.framebuffer.unbindFramebuffer();
        Framebuffer.framebuffer.renderFramebuffer(displayWidth, displayHeight);
        if (Framebuffer.framebuffer.width != displayWidth || Framebuffer.framebuffer.height != displayHeight)
            Framebuffer.framebuffer.createBindFramebuffer(displayWidth, displayHeight);
    }

    @Inject(
            method = "runTick",
            at = @At(
                    value = "RETURN"
            )
    )
    public void runTick(CallbackInfo ci)
    {
        Rose.bus().post(new TickEvent());
    }

    @Inject(
            method = "changeWorld",
            at = @At(
                    value = "RETURN"
            )
    )
    public void onWorldChange(World world, String entityPlayer, EntityPlayer par3, CallbackInfo ci)
    {
        if (world == null)
            return;
        Rose.bus().post(new WorldChangeEvent());
    }

    @Inject(
            method = "clickMouse",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/src/PlayerController;clickBlock(IIII)V"
            )
    )
    public void clickBlock(int par1, CallbackInfo ci)
    {
        Rose.bus().post(new PlayerBlockClickEvent(objectMouseOver.blockX, objectMouseOver.blockY, objectMouseOver.blockZ, objectMouseOver.sideHit));
    }
}