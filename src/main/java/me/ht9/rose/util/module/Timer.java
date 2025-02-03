package me.ht9.rose.util.module;

public final class Timer
{
    private long time;

    public Timer()
    {
        this.time = System.nanoTime();
    }

    public boolean hasReached(long ms)
    {
        return this.hasReached(ms, false);
    }

    public boolean hasReached(long ms, boolean reset)
    {
        if ((System.nanoTime() - this.time) / 1000000.0 >= ms)
        {
            if (reset)
            {
                this.reset();
            }
            return true;
        }
        return false;
    }

    public long time()
    {
        return System.nanoTime() - this.time;
    }

    public void reset()
    {
        this.time = System.nanoTime();
    }
}