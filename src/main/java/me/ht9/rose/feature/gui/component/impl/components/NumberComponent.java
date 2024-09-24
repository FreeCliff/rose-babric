package me.ht9.rose.feature.gui.component.impl.components;

import me.ht9.rose.feature.gui.RoseGui;
import me.ht9.rose.feature.module.setting.Setting;
import me.ht9.rose.util.render.Render2d;

import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

public final class NumberComponent extends SettingComponent<Number>
{
    private final NumberType type;
    private boolean dragging;

    public NumberComponent(Setting<Number> setting, ModuleComponent parent)
    {
        super(setting, parent);
        if (setting.value() instanceof Integer)
        {
            this.type = NumberType.INTEGER;
        } else if (setting.value() instanceof Double)
        {
            this.type = NumberType.DOUBLE;
        } else if (setting.value() instanceof Float)
        {
            this.type = NumberType.FLOAT;
        } else
        {
            throw new IllegalArgumentException("Unknown type parameter for number setting: " + setting.value().getClass());
        }
    }

    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        float alpha = RoseGui.instance().universalTransparency() / 255.0F;

        Color grey = new Color(15.0F / 255.0F, 15.0F / 255.0F, 15.0F / 255.0F, alpha);

        Color enabled = this.defaultColor();

        if (this.isMouseOverThis(mouseX, mouseY))
        {
            float[] hsb = Color.RGBtoHSB(
                    grey.getRed(),
                    grey.getGreen(),
                    grey.getBlue(),
                    null
            );

            hsb[2] = Math.min(hsb[2] + 0.02F, 1.0F);

            grey = Color.getHSBColor(hsb[0], hsb[1], hsb[2]);

            hsb = Color.RGBtoHSB(
                    enabled.getRed(),
                    enabled.getGreen(),
                    enabled.getBlue(),
                    null
            );

            hsb[2] = Math.min(hsb[2] + 0.2F, 1.0F);

            enabled = Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
        }

        Render2d.drawRect(
                this.x() + 1.5F,
                this.y(),
                this.width() - 3.0F,
                this.height(),
                grey
        );

        @SuppressWarnings("raw")
        Setting raw = this.setting();

        if (this.dragging)
        {
            float width = Math.max(mouseX - (this.x() + 1.5F), 0.0F);
            width = Math.min(width, this.width() - 3.0F) / (this.width() - 3.0F);
            switch (this.type)
            {
                case INTEGER:
                {
                    Setting<Integer> intSetting = (Setting<Integer>) raw;
                    int max = (int) intSetting.max() - (int) intSetting.min();
                    int newValue = (int) (width * max) + (int) intSetting.min();
                    intSetting.setValue(newValue);
                    break;
                }
                case DOUBLE:
                {
                    Setting<Double> doubleSetting = (Setting<Double>) raw;
                    double max = (double) doubleSetting.max() - (double) doubleSetting.min();
                    double newValue = this.roundDouble((width * max) + (double) doubleSetting.min(), doubleSetting.roundingScale());
                    doubleSetting.setValue(newValue);
                    break;
                }
                case FLOAT:
                {
                    Setting<Float> floatSetting = (Setting<Float>) raw;
                    float max = (float) floatSetting.max() - (float) floatSetting.min();
                    float newValue = this.roundFloat((width * max) + (float) floatSetting.min(), floatSetting.roundingScale());
                    floatSetting.setValue(newValue);
                    break;
                }
            }
        }

        float percentFilled = 0.0F;
        switch (this.type)
        {
            case INTEGER:
            {
                Setting<Integer> intSetting = (Setting<Integer>) raw;

                int value = intSetting.value() - (int) intSetting.min();
                int max = (int) intSetting.max() - (int) intSetting.min();

                percentFilled = (float) value / max;
                break;
            }
            case DOUBLE:
            {
                Setting<Double> doubleSetting = (Setting<Double>) raw;

                double value = doubleSetting.value() - (double) doubleSetting.min();
                double max = (double) doubleSetting.max() - (double) doubleSetting.min();

                percentFilled = (float) (value / max);
                break;
            }
            case FLOAT:
            {
                Setting<Float> floatSetting = (Setting<Float>) raw;

                float value = floatSetting.value() - (float) floatSetting.min();
                float max = (float) floatSetting.max() - (float) floatSetting.min();

                percentFilled = value / max;
                break;
            }
        }

        float sliderWidth = ((this.width() - 3.0F) * percentFilled);
        sliderWidth = Math.min(sliderWidth, this.width() - 3.0F);

        Render2d.drawRect(
                this.x() + 1.5F,
                this.y(),
                sliderWidth,
                this.height(),
                enabled
        );

        Render2d.drawStringWithShadow(
                this.setting().name(),
                this.x() + 3.0F,
                this.y() + 4.0F,
                new Color(1.0F, 1.0F, 1.0F, alpha)
        );

        String value = "";
        switch (this.type)
        {
            case INTEGER:
            {
                Setting<Integer> intSetting = (Setting<Integer>) raw;
                value = String.valueOf(intSetting.value().doubleValue());
                break;
            }
            case DOUBLE:
            {
                Setting<Double> doubleSetting = (Setting<Double>) raw;
                value = String.valueOf(doubleSetting.value());
                break;
            }
            case FLOAT:
            {
                Setting<Float> floatSetting = (Setting<Float>) raw;
                value = String.valueOf(Double.valueOf(floatSetting.value().toString()).doubleValue());
                break;
            }
        }

        Color textGrey = new Color(180.0F / 255.0F, 180.0F / 255.0F, 180.0F / 255.0F, alpha);

        Render2d.drawStringWithShadow(
                value,
                this.x() + width() - Render2d.stringWidth(value) - 3.0F,
                this.y() + 4.0F,
                textGrey
        );
    }

    @Override
    public void onLeftClick(int mouseX, int mouseY)
    {
        if (this.isMouseOverThis(mouseX, mouseY))
        {
            this.playClickSound();
            this.dragging = !this.dragging;
        }
    }

    @Override
    public void onRightClick(int mouseX, int mouseY)
    {
    }

    @Override
    public void onLeftRelease(int mouseX, int mouseY)
    {
        this.dragging = false;
    }

    @Override
    public void onRightRelease(int mouseX, int mouseY)
    {
    }

    @Override
    public void onMiddleClick(int mouseX, int mouseY)
    {
    }

    @Override
    public void onMiddleRelease(int mouseX, int mouseY)
    {
    }

    @Override
    public void onSideButtonClick(int mouseX, int mouseY, SideButton sideButton)
    {
    }

    @Override
    public void onSideButtonRelease(int mouseX, int mouseY, SideButton sideButton)
    {
    }

    @Override
    public void keyTyped(char typedChar, int keyCode)
    {
    }

    private double roundDouble(double number, int scale)
    {
        return new BigDecimal(number).setScale(scale, RoundingMode.HALF_UP).doubleValue();
    }

    private float roundFloat(float number, int scale)
    {
        return new BigDecimal(number).setScale(scale, RoundingMode.HALF_UP).floatValue();
    }

    private enum NumberType
    {
        INTEGER,
        DOUBLE,
        FLOAT
    }
}