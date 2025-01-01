package me.ht9.rose.event.events;

import me.ht9.rose.event.Event;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.ItemStack;

public final class ItemRenderEvent extends Event
{
    private final EntityLiving entity;
    private final ItemStack itemStack;

    public ItemRenderEvent(EntityLiving entity, ItemStack itemStack) {
        this.entity = entity;
        this.itemStack = itemStack;
    }

    public EntityLiving entity()
    {
        return entity;
    }

    public ItemStack itemStack()
    {
        return itemStack;
    }
}
