package me.ht9.rose.feature;

import me.ht9.rose.util.Globals;

public abstract class Feature implements Globals
{
    protected String name;
    protected String[] aliases;

    public String getName()
    {
        return this.name;
    }

    public String[] getAliases()
    {
        return this.aliases;
    }
}