package me.ht9.rose.mixin.mixins;

import me.ht9.rose.Rose;
import me.ht9.rose.event.events.PlayerMoveEvent;
import me.ht9.rose.event.events.PushByEvent;
import me.ht9.rose.feature.module.modules.exploit.sneak.Sneak;
import net.minecraft.src.Entity;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static me.ht9.rose.util.Globals.mc;

@Mixin(value = Entity.class)
public abstract class MixinEntity
{
    @Unique private boolean shouldInject = true;

    @Inject(
            method = "applyEntityCollision",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    public void applyEntityCollision(Entity entity, CallbackInfo ci)
    {
        PushByEvent event = new PushByEvent(entity, PushByEvent.Pusher.ENTITY);
        Rose.bus().post(event);
        if (event.cancelled())
            ci.cancel();
    }

    @Inject(
            method = "moveEntity",
            at = @At(
                    "HEAD"
            ),
            cancellable = true
    )
    public void moveEntity(double x, double y, double z, CallbackInfo ci)
    {
        Entity $this = (Entity) (Object) this;
        // noinspection ConstantConditions
        if ($this == mc.thePlayer && this.shouldInject)
        {
            PlayerMoveEvent event = new PlayerMoveEvent(x, y, z);
            Rose.bus().post(event);
            this.shouldInject = false;
            $this.moveEntity(event.x(), event.y(), event.z());
            this.shouldInject = true;
            ci.cancel();
        }
    }

    @Redirect(
            method = "moveEntity",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/src/Entity;isSneaking()Z"
            )
    )
    public boolean isSneaking$Head(Entity instance)
    {
        if (Sneak.instance().enabled() && !Keyboard.isKeyDown(mc.gameSettings.keyBindSneak.keyCode))
        {
            return false;
        }
        return instance.isSneaking();
    }
}
