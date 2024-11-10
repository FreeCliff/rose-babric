package me.ht9.rose.event.events;

import me.ht9.rose.event.Event;
import net.minecraft.src.Block;

public final class BlockRenderSideEvent extends Event {
    private final Block block;
    private boolean forceRender;

    public BlockRenderSideEvent(Block block) {
        this.block = block;
    }

    public Block block() {
        return block;
    }

    public boolean forceRender() {
        return forceRender;
    }

    public void setForceRender(boolean forceRender) {
        this.forceRender = forceRender;
    }
}
