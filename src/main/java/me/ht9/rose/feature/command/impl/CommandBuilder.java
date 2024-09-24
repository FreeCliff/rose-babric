package me.ht9.rose.feature.command.impl;

import com.llamalad7.mixinextras.lib.apache.commons.tuple.ImmutablePair;
import me.ht9.rose.feature.command.Command;

import java.util.ArrayList;
import java.util.List;

public final class CommandBuilder
{
    private final String name;
    private Executable executable;
    private String description;
    private final List<ImmutablePair<String, ArgumentSupplier>> suggestionMap = new ArrayList<>();

    public CommandBuilder(String name) {
        this.name = name;
    }

    public CommandBuilder withSuggestion(String key, ArgumentSupplier suggestions)
    {
        this.suggestionMap.add(new ImmutablePair<>(key, suggestions));
        return this;
    }

    public CommandBuilder withExecutable(Executable executable)
    {
        this.executable = executable;
        return this;
    }

    public CommandBuilder withDescription(String description)
    {
        this.description = description;
        return this;
    }

    public void run(String[] args)
    {
        this.executable.accept(args);
    }

    public Command asCommand()
    {
        return new Command(this.name, this.description, this.executable, this.suggestionMap);
    }
}