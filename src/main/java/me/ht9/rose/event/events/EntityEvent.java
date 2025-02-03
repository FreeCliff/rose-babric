package me.ht9.rose.event.events;

import me.ht9.rose.event.Event;
import net.minecraft.src.Entity;

public class EntityEvent extends Event
{
    private final Entity entity;

    public EntityEvent(Entity entity)
    {
        this.entity = entity;
    }

    public Entity entity()
    {
        return entity;
    }

    public enum Type
    {
        ENTITY,
        PLAYER
    }

    public static final class EntityJoinEvent extends EntityEvent
    {
        public EntityJoinEvent(Entity entity)
        {
            super(entity);
        }
    }

    public static final class EntityLeaveEvent extends EntityEvent
    {

        public EntityLeaveEvent(Entity entity)
        {
            super(entity);
        }
    }
}
