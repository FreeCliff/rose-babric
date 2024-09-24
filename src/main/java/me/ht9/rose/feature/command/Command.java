package me.ht9.rose.feature.command;

import com.llamalad7.mixinextras.lib.apache.commons.tuple.ImmutablePair;
import me.ht9.rose.feature.command.impl.ArgumentSupplier;
import me.ht9.rose.feature.command.impl.Executable;

import java.util.List;

public record Command(
        String name,
        String description,
        Executable executable,
        List<ImmutablePair<String, ArgumentSupplier>> suggestionMap
)
{
    public void execute(String[] args)
    {
        this.executable.accept(args);
    }
}