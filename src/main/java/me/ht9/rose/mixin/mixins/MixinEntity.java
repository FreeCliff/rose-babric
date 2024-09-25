package me.ht9.rose.mixin.mixins;

import me.ht9.rose.Rose;
import me.ht9.rose.event.events.PushByEvent;
import net.minecraft.src.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Entity.class)
public abstract class MixinEntity
{
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
        {
            ci.cancel();
        }
    }
}
