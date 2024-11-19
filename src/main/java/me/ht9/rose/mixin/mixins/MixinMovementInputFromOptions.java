package me.ht9.rose.mixin.mixins;

import me.ht9.rose.Rose;
import me.ht9.rose.event.events.MoveStateUpdateEvent;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.MovementInputFromOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MovementInputFromOptions.class)
public class MixinMovementInputFromOptions
{
    @Shadow private boolean[] movementKeyStates;

    @Inject(method = "updatePlayerMoveState", at = @At("HEAD"), cancellable = true)
    public void updatePlayerMoveState(EntityPlayer par1, CallbackInfo ci)
    {
        boolean moveForward = movementKeyStates[0];
        boolean moveBack = movementKeyStates[1];
        boolean moveLeft = movementKeyStates[2];
        boolean moveRight = movementKeyStates[3];
        boolean jump = movementKeyStates[4];
        boolean sneak = movementKeyStates[5];

        MoveStateUpdateEvent event = new MoveStateUpdateEvent(moveForward, moveBack, moveLeft, moveRight, jump, sneak);
        Rose.bus().post(event);

        movementKeyStates[0] = event.moveForward();
        movementKeyStates[1] = event.moveBack();
        movementKeyStates[2] = event.moveLeft();
        movementKeyStates[3] = event.moveRight();
        movementKeyStates[4] = event.jump();
        movementKeyStates[5] = event.sneak();

        if (event.cancelled())
            ci.cancel();
    }
}
