package me.ht9.rose.event.bus;

import me.ht9.rose.Rose;
import me.ht9.rose.event.Event;
import me.ht9.rose.event.bus.annotation.SubscribeEvent;
import me.ht9.rose.event.bus.reflection.ReflectUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public final class EventBus
{
    private final Invoker headInvoker;

    public EventBus()
    {
        // null head invoker
        this.headInvoker = new Invoker(null, null, null, 0);
    }

    public void register(Object subscriber)
    {
        if (subscriber instanceof Class<?>)
        {
            this.registerStaticSubscriber((Class<?>) subscriber);
        } else this.registerVirtualSubscriber(subscriber);
    }

    private void registerStaticSubscriber(Class<?> subscriber)
    {
        Method[] methods = ReflectUtils.getDeclaredMethods(
                subscriber,
                method -> method.isAnnotationPresent(SubscribeEvent.class)
                        && method.getParameters().length == 1
                        && Event.class.isAssignableFrom(method.getParameters()[0].getType())
                        && Modifier.isStatic(method.getModifiers())
        );

        for (Method method : methods)
        {
            this.registerMethod(method, subscriber);
        }
    }

    private void registerVirtualSubscriber(Object subscriber)
    {
        Method[] methods = ReflectUtils.getDeclaredMethods(
                subscriber.getClass(),
                method -> method.isAnnotationPresent(SubscribeEvent.class)
                        && method.getParameters().length == 1
                        && Event.class.isAssignableFrom(method.getParameters()[0].getType())
                        && !Modifier.isStatic(method.getModifiers())
        );

        for (Method method : methods)
        {
            this.registerMethod(method, subscriber);
        }
    }

    public void registerMethod(Method method, Object instance)
    {
        Invoker invoker;
        Class<?> eventType = method.getParameters()[0].getType();
        int priority = method.getDeclaredAnnotation(SubscribeEvent.class).priority();
        if (instance instanceof Class<?>)
        {
            Class<?> parent = (Class<?>) instance;
            invoker = new Invoker(method, parent, eventType, priority);
        } else
        {
            invoker = new Invoker(method, instance, eventType, priority);
        }
        this.insertInvoker(invoker);
    }

    private void insertInvoker(Invoker invoker)
    {
        Invoker prev = this.headInvoker;
        Invoker current = prev.next;

        while (current != null && invoker.priority < current.priority)
        {
            prev = current;
            current = current.next;
        }

        prev.next = invoker;
        invoker.next = current;
    }

    public void unregister(Object subscriber)
    {
        Invoker prev = this.headInvoker;
        Invoker tmp = prev.next;

        while (tmp != null && tmp.instance.equals(subscriber))
        {
            prev.next = tmp.next;
            tmp = tmp.next;
        }

        while (tmp != null)
        {
            while (tmp != null && !tmp.instance.equals(subscriber))
            {
                prev = tmp;
                tmp = tmp.next;
            }

            if (tmp != null)
            {
                prev.next = tmp.next;
                tmp = prev.next;
            }
        }
    }

    public void post(Event event)
    {
        Class<? extends Event> eventType = event.getClass();

        Invoker current = this.headInvoker.next;
        while (current != null)
        {
            if (eventType.equals(current.eventParam))
            {
                this.tryInvokeMethod(current.method, current.instance, event);
            }
            current = current.next;
        }
    }

    private void tryInvokeMethod(Method method, Object instance, Event event)
    {
        try
        {
            method.setAccessible(true);
            method.invoke(instance, event);
        } catch (IllegalAccessException | InvocationTargetException e)
        {
            Rose.logger().error("[EventBus] Failed to invoke " + event.getClass().getSimpleName() + ": ", e);
        }
    }

    private static final class Invoker
    {
        private final Method method;
        private final Object instance;
        private final Class<?> eventParam;
        private final int priority;
        private Invoker next;

        private Invoker(Method method, Object instance, Class<?> eventParam, int priority)
        {
            this.method = method;
            this.instance = instance;
            this.eventParam = eventParam;
            this.priority = priority;
            this.next = null;
        }
    }
}
