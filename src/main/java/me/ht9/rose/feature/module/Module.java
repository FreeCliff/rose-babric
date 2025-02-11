package me.ht9.rose.feature.module;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import me.ht9.rose.Rose;
import me.ht9.rose.event.events.ModuleEvent;
import me.ht9.rose.feature.Feature;
import me.ht9.rose.feature.module.annotation.Aliases;
import me.ht9.rose.feature.module.annotation.Description;
import me.ht9.rose.feature.module.annotation.DisplayName;
import me.ht9.rose.feature.module.keybinding.Bind;
import me.ht9.rose.feature.module.modules.Category;
import me.ht9.rose.feature.module.setting.Setting;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public abstract class Module extends Feature
{
    private boolean enabled = false;

    private final Category category;
    private final String description;
    private Supplier<String> arrayListInfo;
    private final List<Setting<?>> settings = new ArrayList<>();

    private final Setting<Bind> toggleBind;
    private final Setting<BindMode> bindMode;
    private final Setting<Boolean> drawn;

    protected Module()
    {
        this.name = this.getClass().isAnnotationPresent(DisplayName.class) ? this.getClass().getAnnotation(DisplayName.class).value() : this.getClass().getSimpleName();
        this.aliases = this.getClass().isAnnotationPresent(Aliases.class) ? this.getClass().getAnnotation(Aliases.class).value() : new String[] { };
        this.description = this.getClass().isAnnotationPresent(Description.class) ? this.getClass().getAnnotation(Description.class).value() : "";

        Optional<Category> matched = Category.matchCategory(this.getClass().getPackage().getName());

        if (matched.isPresent())
        {
            this.category = matched.get();
        } else throw new IllegalStateException("Couldn't match category for module " + this.name);

        this.arrayListInfo = () -> "";

        this.toggleBind = new Setting<>(
                "Bind",
                new Bind(Keyboard.KEY_NONE, Bind.BindType.KEYBOARD).withAction(this::toggle)
        );

        this.bindMode = new Setting<>("Toggle", BindMode.Normal);

        this.drawn = new Setting<>("Drawn", true)
                .withOnChange(
                        val ->
                        {
                            ModuleEvent event = new ModuleEvent(this, val ? ModuleEvent.Type.DRAW : ModuleEvent.Type.UNDRAW);
                            Rose.bus().post(event);
                        }
                );
    }

    @SuppressWarnings("EmptyMethod")
    public void onLogIn()
    {
    }

    public void onEnable()
    {
    }

    public void onRender2d(float partialTicks)
    {
    }

    @SuppressWarnings("EmptyMethod")
    public void onRender3d(float partialTicks)
    {
    }

    public void initGL()
    {
    }

    public void onDisable()
    {
    }

    @SuppressWarnings("EmptyMethod")
    public void onLogOut()
    {
    }

    public final void toggle()
    {
        if (this.enabled)
        {
            this.disable();
        } else
        {
            this.enable();
        }
    }

    public final void enable()
    {
        if (!this.enabled)
        {
            this.enabled = true;
            Rose.bus().register(this);
            if (this.category != Category.Hidden)
            {
                Rose.bus().post(new ModuleEvent(this, ModuleEvent.Type.ENABLE));
            }
            if (mc.thePlayer != null && mc.theWorld != null)
            {
                this.onEnable();
            }
        }
    }

    public final void disable()
    {
        if (this.enabled)
        {
            this.enabled = false;
            if (this.category != Category.Hidden)
            {
                Rose.bus().post(new ModuleEvent(this, ModuleEvent.Type.DISABLE));
            }
            Rose.bus().unregister(this);
            if (mc.thePlayer != null && mc.theWorld != null)
            {
                this.onDisable();
            }
        }
    }

    public JsonObject serialize()
    {
        JsonObject module = new JsonObject();
        JsonObject settings = new JsonObject();
        module.add("enabled", new JsonPrimitive(this.enabled));
        for (Setting<?> setting : this.settings)
        {
            if (setting.value() instanceof Boolean)
            {
                settings.add(setting.name(), new JsonPrimitive((Boolean) setting.value()));
            } else if (setting.value() instanceof Integer)
            {
                settings.add(setting.name(), new JsonPrimitive((Integer) setting.value()));
            } else if (setting.value() instanceof Double)
            {
                settings.add(setting.name(), new JsonPrimitive((Double) setting.value()));
            } else if (setting.value() instanceof Float)
            {
                settings.add(setting.name(), new JsonPrimitive((Float) setting.value()));
            } else if (setting.value() instanceof String)
            {
                settings.add(setting.name(), new JsonPrimitive((String) setting.value()));
            } else if (setting.value() instanceof Enum<?>)
            {
                settings.add(setting.name(), new JsonPrimitive(((Enum<?>) setting.value()).name()));
            } else if (setting.value() instanceof Bind)
            {
                JsonObject bind = new JsonObject();
                bind.add("key", new JsonPrimitive(((Bind) setting.value()).key()));
                bind.add("type", new JsonPrimitive(((Bind) setting.value()).type().name()));
                settings.add(setting.name(), bind);
            }
        }
        module.add("settings", settings);
        return module;
    }

    @SuppressWarnings("unchecked")
    public void deserialize(JsonObject object)
    {
        if (object.get("enabled").getAsBoolean())
        {
            this.enable();
        }
        else
        {
            this.disable();
        }
        JsonObject settings = object.get("settings").getAsJsonObject();
        for (Setting<?> setting : this.settings)
        {
            JsonElement element = settings.get(setting.name());
            if (element != null)
            {
                if (setting.value() instanceof Boolean)
                {
                    ((Setting<Boolean>) setting).setValue(element.getAsBoolean(), false);
                } else if (setting.value() instanceof Integer)
                {
                    ((Setting<Integer>) setting).setValue(element.getAsInt(), false);
                } else if (setting.value() instanceof Double)
                {
                    ((Setting<Double>) setting).setValue(element.getAsDouble(), false);
                } else if (setting.value() instanceof Float)
                {
                    ((Setting<Float>) setting).setValue(element.getAsFloat(), false);
                } else if (setting.value() instanceof String)
                {
                    ((Setting<String>) setting).setValue(element.getAsString(), false);
                } else if (setting.value() instanceof Enum<?>)
                {
                    String enumName = element.getAsString();
                    for (Enum<?> e : ((Setting<Enum<?>>) setting).value().getClass().getEnumConstants())
                    {
                        if (e.name().equalsIgnoreCase(enumName))
                        {
                            ((Setting<Enum<?>>) setting).setValue(e, false);
                            break;
                        }
                    }
                } else if (setting.value() instanceof Bind)
                {
                    JsonObject bind = element.getAsJsonObject();
                    int key = bind.get("key").getAsInt();
                    Bind.BindType type = null;
                    String enumName = bind.get("type").getAsString();
                    for (Bind.BindType e : Bind.BindType.values())
                    {
                        if (e.name().equalsIgnoreCase(enumName))
                        {
                            type = e;
                        }
                    }
                    ((Setting<Bind>) setting).value().setKey(key, type);
                }
            }
        }
    }

    public final Category category()
    {
        return this.category;
    }

    public final String description()
    {
        return this.description;
    }

    public final String arraylistInfo()
    {
        return this.arrayListInfo.get();
    }

    public final boolean enabled()
    {
        return this.enabled;
    }

    public final List<Setting<?>> settings()
    {
        return this.settings;
    }

    public final Setting<Bind> toggleBind()
    {
        return this.toggleBind;
    }

    public final Setting<BindMode> bindMode()
    {
        return this.bindMode;
    }

    public final Setting<Boolean> drawn()
    {
        return this.drawn;
    }

    public void setArrayListInfo(Supplier<String> arrayListInfo)
    {
        this.arrayListInfo = arrayListInfo;
    }

    public enum BindMode
    {
        Normal,
        Hold
    }
}