package me.ht9.rose.util.module;

public final class Timer
{
    private long time;

    public Timer()
    {
        this.reset();
    }

    public boolean hasReached(double s)
    {
        return this.hasReached(s, false);
    }

    public boolean hasReached(double s, boolean reset)
    {
        if (this.time() >= s)
        {
            if (reset)
                this.reset();
            return true;
        }
        return false;
    }

    public double time()
    {
        return (System.nanoTime() - this.time) / 1000000000.0;
    }

    public void reset()
    {
        this.time = System.nanoTime();
    }
}