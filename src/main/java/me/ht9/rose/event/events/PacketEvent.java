package me.ht9.rose.event.events;

import me.ht9.rose.event.Event;
import net.minecraft.src.Packet;

public final class PacketEvent extends Event
{
    private final Packet packet;
    private final boolean serverBound;

    public PacketEvent(Packet packet, boolean serverBound)
    {
        this.packet = packet;
        this.serverBound = serverBound;
    }

    public Packet packet()
    {
        return this.packet;
    }

    public boolean serverBound()
    {
        return this.serverBound;
    }
}
