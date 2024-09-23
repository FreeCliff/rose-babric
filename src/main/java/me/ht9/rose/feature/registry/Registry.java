package me.ht9.rose.feature.registry;

import me.ht9.rose.Rose;
import me.ht9.rose.feature.module.setting.Setting;
import me.ht9.rose.feature.module.Module;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public final class Registry
{
    private static final List<Module> modules = new ArrayList<>();

    public static void loadModules()
    {

        modules.forEach(module ->
        {
            for (Field field : module.getClass().getDeclaredFields())
            {
                if (Setting.class.isAssignableFrom(field.getType()))
                {
                    try
                    {
                        field.setAccessible(true);
                        module.settings().add((Setting<?>) field.get(module));
                    } catch (Throwable t)
                    {
                        Rose.logger().error("Failed to instantiate setting {}", field.getName());
                        t.printStackTrace();
                    }
                }
            }
            module.settings().add(module.drawn());
            module.settings().add(module.bindMode());
            module.settings().add(module.toggleBind());
        });
    }

    public static List<Module> modules()
    {
        return modules;
    }
}
