package me.ht9.rose.util.module;

public final class Timer
{
    private double time;

    public Timer()
    {
        this.time = System.currentTimeMillis();
    }

    public boolean hasReached(double ms)
    {
        return this.hasReached(ms, false);
    }

    public boolean hasReached(double ms, boolean reset)
    {
        if (System.currentTimeMillis() - this.time >= ms)
        {
            if (reset)
            {
                this.reset();
            }
            return true;
        }
        return false;
    }

    public double time()
    {
        return System.currentTimeMillis() - this.time;
    }

    public void reset()
    {
        this.time = System.currentTimeMillis();
    }
}