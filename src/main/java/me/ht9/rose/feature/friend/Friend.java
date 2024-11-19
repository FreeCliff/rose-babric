package me.ht9.rose.feature.friend;

import net.minecraft.src.EntityPlayer;

public final class Friend
{
    private final String name;

    public Friend(String name)
    {
        this.name = name;
    }

    public Friend(EntityPlayer player)
    {
        this(player.username);
    }

    public String name() {
        return name;
    }
}
