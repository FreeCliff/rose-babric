package me.ht9.rose.mixin.mixins;

import me.ht9.rose.Rose;
import me.ht9.rose.event.events.MoveStateUpdateEvent;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.MovementInputFromOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MovementInputFromOptions.class)
public class MixinMovementInputFromOptions
{
    @Shadow private boolean[] movementKeyStates;

    @Unique private boolean oldMoveForward;
    @Unique private boolean oldMoveBack;
    @Unique private boolean oldMoveLeft;
    @Unique private boolean oldMoveRight;
    @Unique private boolean oldJump;
    @Unique private boolean oldSneak;

    @Unique private boolean dontReset;

    @Inject(
            method = "updatePlayerMoveState",
            at = @At(
                    "HEAD"
            )
    )
    public void updatePlayerMoveState$Head(EntityPlayer player, CallbackInfo ci)
    {
        boolean moveForward = movementKeyStates[0];
        boolean moveBack = movementKeyStates[1];
        boolean moveLeft = movementKeyStates[2];
        boolean moveRight = movementKeyStates[3];
        boolean jump = movementKeyStates[4];
        boolean sneak = movementKeyStates[5];

        this.oldMoveForward = moveForward;
        this.oldMoveBack = moveBack;
        this.oldMoveLeft = moveLeft;
        this.oldMoveRight = moveRight;
        this.oldJump = jump;
        this.oldSneak = sneak;

        MoveStateUpdateEvent event = new MoveStateUpdateEvent(moveForward, moveBack, moveLeft, moveRight, jump, sneak);
        Rose.bus().post(event);

        movementKeyStates[0] = event.moveForward();
        movementKeyStates[1] = event.moveBack();
        movementKeyStates[2] = event.moveLeft();
        movementKeyStates[3] = event.moveRight();
        movementKeyStates[4] = event.jump();
        movementKeyStates[5] = event.sneak();

        this.dontReset = event.dontReset();
    }

    @Inject(method = "updatePlayerMoveState", at = @At("TAIL"))
    public void updatePlayerMoveState$Tail(EntityPlayer player, CallbackInfo ci)
    {
        if (this.dontReset) return;

        movementKeyStates[0] = this.oldMoveForward;
        movementKeyStates[1] = this.oldMoveBack;
        movementKeyStates[2] = this.oldMoveLeft;
        movementKeyStates[3] = this.oldMoveRight;
        movementKeyStates[4] = this.oldJump;
        movementKeyStates[5] = this.oldSneak;
    }
}
