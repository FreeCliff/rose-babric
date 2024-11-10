package me.ht9.rose.event.events;

import me.ht9.rose.event.Event;

public final class BlockHitEvent extends Event {
    private final int x;
    private final int y;
    private final int z;
    private final int side;

    public BlockHitEvent(int x, int y, int z, int side) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.side = side;
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    public int z() {
        return z;
    }

    public int side() {
        return side;
    }
}
