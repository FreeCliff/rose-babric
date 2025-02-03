package me.ht9.rose.event.events;

import me.ht9.rose.event.Event;

public class MoveStateUpdateEvent extends Event
{
    private boolean moveForward;
    private boolean moveBack;
    private boolean moveLeft;
    private boolean moveRight;
    private boolean jump;
    private boolean sneak;

    private boolean dontReset = false;

    public MoveStateUpdateEvent(boolean moveForward, boolean moveBack, boolean moveLeft, boolean moveRight, boolean jump, boolean sneak)
    {
        this.moveForward = moveForward;
        this.moveBack = moveBack;
        this.moveLeft = moveLeft;
        this.moveRight = moveRight;
        this.jump = jump;
        this.sneak = sneak;
    }

    public boolean moveForward()
    {
        return moveForward;
    }

    public boolean moveBack()
    {
        return moveBack;
    }

    public boolean moveLeft()
    {
        return moveLeft;
    }

    public boolean moveRight()
    {
        return moveRight;
    }

    public boolean jump()
    {
        return jump;
    }

    public boolean sneak()
    {
        return sneak;
    }

    public boolean dontReset()
    {
        return dontReset;
    }

    public void setMoveForward(boolean moveForward)
    {
        this.moveForward = moveForward;
    }

    public void setMoveBack(boolean moveBack)
    {
        this.moveBack = moveBack;
    }

    public void setMoveLeft(boolean moveLeft)
    {
        this.moveLeft = moveLeft;
    }

    public void setMoveRight(boolean moveRight)
    {
        this.moveRight = moveRight;
    }

    public void setJump(boolean jump)
    {
        this.jump = jump;
    }

    public void setSneak(boolean sneak)
    {
        this.sneak = sneak;
    }

    public void setDontReset(boolean dontReset)
    {
        this.dontReset = dontReset;
    }
}
