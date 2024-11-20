package me.ht9.rose.feature.command;

import me.ht9.rose.feature.command.impl.ArgumentSupplier;
import me.ht9.rose.feature.command.impl.Executable;
import org.apache.commons.lang3.tuple.ImmutablePair;

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