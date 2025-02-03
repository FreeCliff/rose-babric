package me.ht9.rose.feature.module.modules.client.hud;

import me.ht9.rose.Rose;
import me.ht9.rose.event.bus.annotation.SubscribeEvent;
import me.ht9.rose.event.events.ModuleEvent;
import me.ht9.rose.feature.module.Module;
import me.ht9.rose.feature.module.annotation.Description;
import me.ht9.rose.feature.module.modules.Category;
import me.ht9.rose.feature.module.modules.client.clickgui.ClickGUI;
import me.ht9.rose.feature.module.setting.Setting;
import me.ht9.rose.feature.registry.Registry;
import me.ht9.rose.util.render.Easing;
import me.ht9.rose.util.render.Render2d;
import net.minecraft.src.GuiChat;
import net.minecraft.src.MathHelper;
import net.minecraft.src.ScaledResolution;

import java.awt.*;
import java.util.*;
import java.util.List;

// I took it from alpha by ht9 and gavin
@Description("Cool")
public final class Hud extends Module
{
    private static final Hud instance = new Hud();

    private final Setting<HudColor> hudColor = new Setting<>("HudColor", HudColor.Static);

    private final Setting<Boolean> waterMark = new Setting<>("WaterMark", true);

    private final Setting<Boolean> arrayList = new Setting<>("ArrayList", false);
    private final Setting<ArraySort> arrayListSort = new Setting<>("ArraySort", ArraySort.Length, arrayList::value);
    private final Setting<ArraySide> arrayListSide = new Setting<>("ArraySide", ArraySide.Top, arrayList::value);
    private final Setting<Integer> arrayAnimMs = new Setting<>("ArrayAnimMS", 0, 200, 500, arrayList::value);

    private final List<ArrayListModule> renderingMods = new ArrayList<>();

    private Hud()
    {
        setArrayListInfo(() -> hudColor.value().toString());
    }

    @SubscribeEvent
    public void onModule(ModuleEvent event)
    {
        if (mc.thePlayer == null || mc.theWorld == null) return;
        Module module = event.module();
        long toggleTime = System.nanoTime() / 1000000L;
        if (module.category().equals(Category.Hidden)) return;

        switch (event.type())
        {
            case DRAW:
            case ENABLE: {
                if (!module.drawn().value()) break;
                for (ArrayListModule arrayListModule : this.renderingMods)
                {
                    if (!arrayListModule.module.equals(module)) continue;
                    arrayListModule.toggleTime = toggleTime;
                    arrayListModule.lastProgress = arrayListModule.progress;
                    return;
                }
                ArrayListModule arrayListModule = new ArrayListModule(module, toggleTime);
                this.renderingMods.add(arrayListModule);
                arrayListModule.lastProgress = -arrayListModule.getStringWidth();
                break;
            }
            case UNDRAW:
            case DISABLE: {
                for (ArrayListModule arrayListModule : this.renderingMods)
                {
                    if (!arrayListModule.module.equals(module)) continue;
                    arrayListModule.toggleTime = toggleTime;
                    arrayListModule.lastProgress = arrayListModule.progress;
                }
                break;
            }
        }
    }

    @Override
    public void onDisable() {
        renderingMods.clear();
    }

    @Override
    public void onRender2d(float partialTicks) {
        if (waterMark.value())
            renderWaterMark();
        if (arrayList.value())
            renderArrayList();
    }

    private void renderWaterMark()
    {
        Render2d.drawGradientStringWithShadow("rose-babric " + Rose.version(), 1.0f, 2.5f, ClickGUI.instance().customFont.value());
    }

    private void renderArrayList()
    {
        for (Module module : Registry.modules())
        {
            if (module.category() == Category.Hidden) continue;
            boolean contains = false;
            for (ArrayListModule arrayListModule : this.renderingMods)
            {
                if (!arrayListModule.module.equals(module)) continue;
                contains = true;
                break;
            }
            if (!module.enabled() || !module.drawn().value() || contains) continue;
            this.renderingMods.add(new ArrayListModule(module, System.nanoTime() / 1000000L));
        }
        this.renderingMods.sort(this.arrayListSort.value().comparator);
        switch (this.arrayListSide.value())
        {
            case Top ->
            {
                float offset = 1.0f;
                Iterator<ArrayListModule> arrayListModuleIterator = this.renderingMods.iterator();
                while (arrayListModuleIterator.hasNext())
                {
                    float diff;
                    ArrayListModule arrayListModule = arrayListModuleIterator.next();
                    float targetX = arrayListModule.getStringWidth();
                    float timeDiff = System.nanoTime() / 1000000F - arrayListModule.toggleTime;
                    float x = arrayListModule.module.enabled() ? Easing.exponential(timeDiff, 0.0f, targetX, this.arrayAnimMs.value() * 2) : Easing.linear(timeDiff, 0.0f, targetX, this.arrayAnimMs.value());
                    if (arrayListModule.module.enabled() && arrayListModule.module.drawn().value())
                    {
                        x -= arrayListModule.getStringWidth();
                    }
                    else
                    {
                        x = -x;
                        if (arrayListModule.progress - 2.0f <= -arrayListModule.getStringWidth())
                        {
                            arrayListModuleIterator.remove();
                        }
                    }
                    if (arrayListModule.module.enabled() && arrayListModule.module.drawn().value())
                    {
                        diff = arrayListModule.getStringWidth() + arrayListModule.lastProgress;
                        x += diff;
                        x = Math.min(x, 0.0f);
                        if (timeDiff > (float) this.arrayAnimMs.value())
                        {
                            x = 0.0f;
                        }
                    }
                    else
                    {
                        diff = arrayListModule.lastProgress;
                        x += diff;
                        x = Math.max(x, -arrayListModule.getStringWidth());
                    }
                    arrayListModule.progress = x;
                    ScaledResolution sr = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
                    if (arrayListModule.hasArrayListInfo())
                    {
                        Render2d.drawGradientStringWithShadow(arrayListModule.module.name(), (float)sr.getScaledWidth() - (x += arrayListModule.getStringWidth()), offset, ClickGUI.instance().customFont.value());
                        Render2d.drawStringWithShadow(" [", (float)sr.getScaledWidth() - (x -= Render2d.getStringWidth(arrayListModule.module.name(), ClickGUI.instance().customFont.value())), offset, new Color(-4473925), ClickGUI.instance().customFont.value());
                        Render2d.drawStringWithShadow(arrayListModule.module.arraylistInfo(), (float)sr.getScaledWidth() - (x -= Render2d.getStringWidth(" [", ClickGUI.instance().customFont.value())), offset, new Color(-1), ClickGUI.instance().customFont.value());
                        Render2d.drawStringWithShadow("]", (float)sr.getScaledWidth() - (x - Render2d.getStringWidth(arrayListModule.module.arraylistInfo(), ClickGUI.instance().customFont.value())), offset, new Color(-4473925), ClickGUI.instance().customFont.value());
                    }
                    else
                    {
                        Render2d.drawGradientStringWithShadow(arrayListModule.module.name(), (float)sr.getScaledWidth() - x - arrayListModule.getStringWidth(), offset, ClickGUI.instance().customFont.value());
                        arrayListModule.progress = x;
                    }
                    offset += 10.0f;
                }
            }
            case Bottom ->
            {
                float offset = 9.0f;
                if (mc.currentScreen instanceof GuiChat) {
                    offset += 16.0f;
                }
                Iterator<ArrayListModule> arrayListModuleIterator = this.renderingMods.iterator();
                while (arrayListModuleIterator.hasNext())
                {
                    float diff;
                    ArrayListModule arrayListModule = arrayListModuleIterator.next();
                    float targetX = arrayListModule.getStringWidth();
                    float timeDiff = System.nanoTime() / 1000000F - arrayListModule.toggleTime;
                    float x = arrayListModule.module.enabled() ? Easing.exponential(timeDiff, 0.0f, targetX, this.arrayAnimMs.value() * 2) : Easing.linear(timeDiff, 0.0f, targetX, this.arrayAnimMs.value().longValue());
                    if (arrayListModule.module.enabled() && arrayListModule.module.drawn().value())
                    {
                        x -= arrayListModule.getStringWidth();
                    }
                    else
                    {
                        x = -x;
                        if (arrayListModule.progress - 2.0f <= -arrayListModule.getStringWidth())
                        {
                            arrayListModuleIterator.remove();
                        }
                    }
                    ScaledResolution sr = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
                    if (arrayListModule.module.enabled() && arrayListModule.module.drawn().value())
                    {
                        diff = arrayListModule.getStringWidth() + arrayListModule.lastProgress;
                        x += diff;
                        x = Math.min(x, 0.0f);
                        if (timeDiff > (float)this.arrayAnimMs.value().longValue())
                        {
                            x = 0.0f;
                        }
                    }
                    else
                    {
                        diff = arrayListModule.lastProgress;
                        x += diff;
                        x = Math.max(x, -arrayListModule.getStringWidth());
                    }
                    arrayListModule.progress = x;
                    if (arrayListModule.hasArrayListInfo())
                    {
                        Render2d.drawGradientStringWithShadow(arrayListModule.module.name(), (float)sr.getScaledWidth() - (x += arrayListModule.getStringWidth()), (float)sr.getScaledHeight() - offset, ClickGUI.instance().customFont.value());
                        Render2d.drawStringWithShadow(" [", (float)sr.getScaledWidth() - (x -= Render2d.getStringWidth(arrayListModule.module.name(), ClickGUI.instance().customFont.value())), (float)sr.getScaledHeight() - offset, new Color(-4473925), ClickGUI.instance().customFont.value());
                        Render2d.drawStringWithShadow(arrayListModule.module.arraylistInfo(), (float)sr.getScaledWidth() - (x -= Render2d.getStringWidth(" [", ClickGUI.instance().customFont.value())), (float)sr.getScaledHeight() - offset, new Color(-1), ClickGUI.instance().customFont.value());
                        Render2d.drawStringWithShadow("]", (float)sr.getScaledWidth() - (x - Render2d.getStringWidth(arrayListModule.module.arraylistInfo(), ClickGUI.instance().customFont.value())), (float)sr.getScaledHeight() - offset, new Color(-4473925), ClickGUI.instance().customFont.value());
                    }
                    else
                    {
                        Render2d.drawGradientStringWithShadow(arrayListModule.module.name(), (float)sr.getScaledWidth() - x - arrayListModule.getStringWidth(), (float)sr.getScaledHeight() - offset, ClickGUI.instance().customFont.value());
                        arrayListModule.progress = x;
                    }
                    offset += 10.0f;
                }
            }
        }
    }

    public Color getColor(float offset)
    {
        return switch (this.hudColor.value())
        {
            case Static -> new Color(ClickGUI.instance().red.value(), ClickGUI.instance().green.value(), ClickGUI.instance().blue.value());
            case Dynamic ->
            {
                Color guiColor = new Color(ClickGUI.instance().red.value(), ClickGUI.instance().green.value(), ClickGUI.instance().blue.value());
                float sin = (System.nanoTime() % 100000000000L) / 1000000F / 10.0f;
                float brightness = Math.abs(MathHelper.sin((float) Math.toRadians(sin + offset)));
                float[] hsb = Color.RGBtoHSB(guiColor.getRed(), guiColor.getGreen(), guiColor.getBlue(), null);
                yield Color.getHSBColor(hsb[0], hsb[1], Math.max(brightness, 0.25f));
            }
            case Rainbow ->
            {
                float sin = (System.nanoTime() % 100000000000L) / 1000000F / 25.0f;
                float hue = 0.4f * Math.abs(MathHelper.sin((float) Math.toRadians(sin + offset / 2.5f))) + 0.6f;
                yield Color.getHSBColor(hue, 0.5f, 1.0f);
            }
        };
    }

    public int getRGBA(Color color)
    {
        if (color.getAlpha() <= 0)
        {
            return 0;
        }
        return this.getRGBA(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }

    public int getRGBA(int r, int g, int b, int a)
    {
        return (r << 16) + (g << 8) + b + (a << 24);
    }

    public static Hud instance()
    {
        return instance;
    }

    private static final class ArrayListModule
    {
        private final Module module;
        private long toggleTime;
        private float progress;
        private float lastProgress;

        private ArrayListModule(Module module, long toggleTime)
        {
            this.module = module;
            this.toggleTime = toggleTime;
            this.lastProgress = -this.getStringWidth();
            this.progress = -this.getStringWidth();
        }

        private boolean hasArrayListInfo()
        {
            return !this.module.arraylistInfo().isEmpty();
        }

        private float getStringWidth()
        {
            String fullName = this.getFullName();
            return Render2d.getStringWidth(fullName, ClickGUI.instance().customFont.value());
        }

        private String getFullName()
        {
            StringBuilder builder = new StringBuilder();
            builder.append(this.module.name());
            if (this.hasArrayListInfo())
            {
                builder.append(" [");
                builder.append(this.module.arraylistInfo());
                builder.append("]");
            }
            return builder.toString();
        }
    }

    public enum HudColor
    {
        Static,
        Dynamic,
        Rainbow
    }

    public enum ArraySide
    {
        Top,
        Bottom
    }

    public enum ArraySort
    {
        Length((m1, m2) -> Float.compare(m2.getStringWidth(), m1.getStringWidth())),
        ABC(Comparator.comparing(ArrayListModule::getFullName));

        private final Comparator<? super ArrayListModule> comparator;

        ArraySort(Comparator<? super ArrayListModule> comparator)
        {
            this.comparator = comparator;
        }
    }

    public enum E621Size
    {
        preview,
        sample,
        file
    }
}
