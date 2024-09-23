package me.ht9.rose.feature.module.modules;

import me.ht9.rose.feature.registry.Registry;
import me.ht9.rose.feature.module.Module;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public enum Category
{
    Client("me.ht9.rose.feature.module.modules.client"),
    Combat("me.ht9.rose.feature.module.modules.combat"),
    Exploit("me.ht9.rose.feature.module.modules.exploit"),
    Movement("me.ht9.rose.feature.module.modules.movement"),
    Render("me.ht9.rose.feature.module.modules.render"),
    Blatant("me.ht9.rose.feature.module.modules.blatant"),
    Hidden("me.ht9.rose.feature.module.modules.hidden");

    private final String packageName;

    Category(String packageName)
    {
        this.packageName = packageName;
    }

    public String getPackage()
    {
        return this.packageName;
    }

    public List<Module> modules()
    {
        return Registry.modules().stream().filter(
                m -> m.category() == this
        ).collect(Collectors.toList());
    }

    public boolean hasModules()
    {
        return !this.modules().isEmpty();
    }

    public static Optional<Category> matchCategory(String packageName)
    {
        return Arrays.stream(Category.values()).filter(
                c -> packageName.startsWith(c.getPackage())
        ).findFirst();
    }
}
