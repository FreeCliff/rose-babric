package me.ht9.rose.event.bus.reflection;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public final class ReflectUtils
{
    public static Method[] getDeclaredMethods(Class<?> clazz, Predicate<? super Method> predicate)
    {
        List<Method> list = new ArrayList<>();

        for (Method method : clazz.getDeclaredMethods())
        {
            if (predicate.test(method))
            {
                list.add(method);
            }
        }

        return list.toArray(new Method[0]);
    }
}