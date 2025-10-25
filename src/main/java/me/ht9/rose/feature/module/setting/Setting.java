package me.ht9.rose.feature.module.setting;

import java.util.function.Consumer;
import java.util.function.Supplier;

public final class Setting<V>
{
    private final String name;
    private String description;
    private final Number min;
    private V value;
    private final V defaultValue;
    private final Number max;
    private final Supplier<Boolean> visibility;
    private int enumIndex = 0;
    private final int roundingScale;
    private Consumer<V> onChange;
    private boolean alwaysCallOnChange = false;

    public <N extends Number> Setting(String name, N min, V value, N max, int roundingScale, Supplier<Boolean> visibility)
    {
        this.name = name;
        this.description = "";
        this.min = min;
        this.value = value;
        this.defaultValue = value;
        this.max = max;
        this.roundingScale = roundingScale;
        this.visibility = visibility;
        this.checkEnums();
    }

    public <N extends Number> Setting(String name, N min, V value, N max, int roundingScale)
    {
        this(name, min, value, max, roundingScale, () -> true);
    }

    public <N extends Number> Setting(String name, N min, V value, N max)
    {
        this(name, min, value, max, 0, () -> true);
    }
    public <N extends Number> Setting(String name, N min, V value, N max, Supplier<Boolean> visibility)
    {
        this(name, min, value, max, 0, visibility);
    }

    public Setting(String name, V value)
    {
        this(name, 0.0F, value, 0.0F, 0, () -> true);
    }

    public Setting(String name, V value, Supplier<Boolean> visibility)
    {
        this(name, 0.0F, value, 0.0F, 0, visibility);
    }

    public Setting<V> withOnChange(Consumer<V> action)
    {
        return this.withOnChange(action, false);
    }

    public Setting<V> withOnChange(Consumer<V> action, boolean alwaysCallOnChange)
    {
        this.onChange = action;
        this.alwaysCallOnChange = alwaysCallOnChange;
        return this;
    }

    public Setting<V> withDescription(String description)
    {
        this.description = description;
        return this;
    }

    private void checkEnums()
    {
        if (this.value instanceof Enum<?>)
        {
            Enum<?>[] enums = (Enum<?>[]) this.value.getClass().getEnumConstants();
            for (int i = 0; i < enums.length; i++)
            {
                if (enums[i] == this.value)
                {
                    this.enumIndex = i;
                    break;
                }
            }
        }
    }

    public void reset()
    {
        this.setValue(this.defaultValue);
    }

    public Enum<?> nextEnum()
    {
        if (this.value instanceof Enum<?>)
        {
            Enum<?>[] enums = (Enum<?>[]) this.value.getClass().getEnumConstants();
            if (++this.enumIndex >= enums.length)
            {
                this.enumIndex = 0;
                return enums[0];
            } else
            {
                return enums[this.enumIndex];
            }
        }
        throw new IllegalStateException("Setting value is not an enum.");
    }

    public void setValue(V value)
    {
        this.setValue(value, true);
    }

    public void setValue(V value, boolean callOnChange)
    {
        this.value = value;
        this.checkEnums();
        if ((callOnChange || this.alwaysCallOnChange) && this.onChange != null)
        {
            this.onChange.accept(value);
        }
    }

    public String name()
    {
        return this.name;
    }

    public String description()
    {
        return description;
    }

    public Number min()
    {
        return this.min;
    }

    public V value()
    {
        return this.value;
    }

    public V defaultValue()
    {
        return this.defaultValue;
    }

    public Number max()
    {
        return this.max;
    }

    public int roundingScale()
    {
        return this.roundingScale;
    }

    public boolean visible()
    {
        return this.visibility.get();
    }
}