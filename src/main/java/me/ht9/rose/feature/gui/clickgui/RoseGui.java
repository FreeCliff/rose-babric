package me.ht9.rose.feature.gui.clickgui;

import me.ht9.rose.feature.gui.component.impl.components.ModuleComponent;
import me.ht9.rose.feature.gui.component.impl.components.SettingComponent;
import me.ht9.rose.feature.gui.component.impl.windows.ModuleWindow;
import me.ht9.rose.feature.module.modules.Category;
import me.ht9.rose.feature.module.modules.client.clickgui.ClickGUI;
import me.ht9.rose.feature.module.setting.Setting;
import me.ht9.rose.util.Globals;
import me.ht9.rose.util.config.FileUtils;
import me.ht9.rose.util.render.Easing;
import me.ht9.rose.feature.module.Module;
import me.ht9.rose.util.render.Render2d;
import me.ht9.rose.feature.gui.component.Component;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.ScaledResolution;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.Iterator;
import java.util.LinkedList;

public class RoseGui extends GuiScreen implements Globals
{
    private static final RoseGui instance = new RoseGui();

    private final LinkedList<ModuleWindow> windows = new LinkedList<>();

    private boolean leftMouseDown;
    private boolean dragging;

    private long lastOpenTime;

    private float universalTransparency;
    private float lastTransparency;

    private float lastScale;

    private int lastMouseX;
    private int lastMouseY;

    private RoseGui()
    {
        float xOff = 30.0F;
        for (Category category : Category.values())
        {
            if (category != Category.Hidden)
            {
                this.windows.add(new ModuleWindow(xOff, 25.0F, category));
                xOff += 135.0F;
            }
        }
    }

    public void openGui()
    {
        this.lastOpenTime = System.currentTimeMillis();
        this.universalTransparency = 255.0F;
        this.lastTransparency = 0.0F;
        this.lastScale = 0.0F;

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        Globals.mc.displayGuiScreen(instance);
    }

    public void openGuiMainMenu()
    {
        this.lastOpenTime = System.currentTimeMillis();
        this.universalTransparency = 0.0F;
        this.lastTransparency = 0.0F;
        this.lastScale = 0.0F;

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        Globals.mc.displayGuiScreen(instance);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        super.drawScreen(mouseX, mouseY, partialTicks);
        super.drawDefaultBackground();

        long timeDiff = System.currentTimeMillis() - this.lastOpenTime;
        float currentTransparency = Easing.linear(timeDiff, 55.0F, 200.0F, 250L);
        this.universalTransparency = this.lastTransparency + (currentTransparency - this.lastTransparency) * partialTicks;
        this.universalTransparency = Math.min(this.universalTransparency, 255.0F);

        GL11.glPushMatrix();

        this.renderAndScaleGUI(partialTicks);

        ModuleWindow primary = this.windows.getFirst();

        if (this.leftMouseDown)
        {
            if (primary.isMouseOverTitle(mouseX, mouseY))
            {
                this.dragging = true;
            }
        } else this.dragging = false;

        if (this.dragging)
        {
            primary.drag(mouseX, mouseY);
        }

        ScaledResolution sr = new ScaledResolution(Globals.mc.gameSettings, Globals.mc.displayWidth, Globals.mc.displayHeight);

        for (Iterator<ModuleWindow> iter = this.windows.descendingIterator(); iter.hasNext();)
        {
            ModuleWindow window = iter.next();

            Component hoveredComponent = null;
            for (ModuleComponent moduleComponent : window.components())
            {
                if (moduleComponent.isMouseOverThis(mouseX, mouseY))
                {
                    hoveredComponent = moduleComponent;
                    break;
                }
                for (SettingComponent<?> settingComponent : moduleComponent.components())
                {
                    if (settingComponent.isMouseOverThis(mouseX, mouseY))
                    {
                        hoveredComponent = settingComponent;
                        break;
                    }
                }
            }

            if (hoveredComponent != null)
            {
                if (hoveredComponent instanceof ModuleComponent component)
                {
                    Module module = component.module();

                    Render2d.drawStringWithShadow(
                            module.description(),
                            2.0F,
                            sr.getScaledHeight() - 10.0F,
                            new Color(180.0F / 255.0F, 180.0F / 255.0F, 180.0F / 255.0F, this.universalTransparency / 255.0F)
                    );
                }
                else if (hoveredComponent instanceof SettingComponent<?> component)
                {
                    Setting<?> setting = component.setting();

                    Render2d.drawStringWithShadow(
                            setting.description(),
                            2.0F,
                            sr.getScaledHeight() - 10.0F,
                            new Color(180.0F / 255.0F, 180.0F / 255.0F, 180.0F / 255.0F, this.universalTransparency / 255.0F)
                    );
                }
            }

            window.drawScreen(mouseX, mouseY, partialTicks);
        }

        GL11.glPopMatrix();

        this.lastTransparency = this.universalTransparency;
        this.lastMouseX = mouseX;
        this.lastMouseY = mouseY;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton)
    {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        switch (mouseButton)
        {
            case 0:
                this.leftMouseDown = true;
                break;
            case 1:
                break;
        }

        this.windows.stream().filter(
                w -> w.isMouseOverWholeTab(mouseX, mouseY)
        ).findFirst().ifPresent(
                w -> {
                    this.windows.remove(w);
                    this.windows.addFirst(w);
                }
        );

        ModuleWindow primary = this.windows.getFirst();

        if (primary.isMouseOverWholeTab(mouseX, mouseY))
        {
            switch (mouseButton)
            {
                case 0:
                    primary.onLeftClick(mouseX, mouseY);
                    break;
                case 1:
                    primary.onRightClick(mouseX, mouseY);
                    break;
                case 2:
                    primary.onMiddleClick(mouseX, mouseY);
                    break;
                case 3:
                    primary.onSideButtonClick(mouseX, mouseY, Component.SideButton.SIDE1);
                    break;
                case 4:
                    primary.onSideButtonClick(mouseX, mouseY, Component.SideButton.SIDE2);
                    break;
            }
        }
    }

    @Override
    public void mouseMovedOrUp(int mouseX, int mouseY, int state)
    {
        super.mouseMovedOrUp(mouseX, mouseY, state);

        switch (state)
        {
            case 0:
                this.leftMouseDown = false;
                break;
            case 1:
                break;
        }

        ModuleWindow primary = this.windows.getFirst();

        switch (state)
        {
            case 0:
                primary.onLeftRelease(mouseX, mouseY);
                break;
            case 1:
                primary.onRightRelease(mouseX, mouseY);
                break;
            case 2:
                primary.onMiddleRelease(mouseX, mouseY);
                break;
            case 3:
                primary.onSideButtonRelease(mouseX, mouseY, Component.SideButton.SIDE1);
                break;
            case 4:
                primary.onSideButtonRelease(mouseX, mouseY, Component.SideButton.SIDE2);
                break;
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode)
    {
        super.keyTyped(typedChar, keyCode);

        this.windows.getFirst().keyTyped(typedChar, keyCode);
    }

    @Override
    public void onGuiClosed()
    {
        super.onGuiClosed();
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        ClickGUI.instance().disable();

        new Thread(() ->
        {
            FileUtils.saveModules();
            FileUtils.saveClickGUI();
            FileUtils.saveFriends();
        }).start();
    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }

    private void renderAndScaleGUI(float partialTicks)
    {
        ScaledResolution sr = new ScaledResolution(Globals.mc.gameSettings, Globals.mc.displayWidth, Globals.mc.displayHeight);

        Render2d.drawRect(
                0.0F,
                0.0F,
                sr.getScaledWidth(),
                sr.getScaledHeight(),
                new Color(16.0F / 255.0F, 16.0F / 255.0F, 16.0F / 255.0F, this.universalTransparency / 510.0F)
        );

        float passedTime = System.currentTimeMillis() - this.lastOpenTime;
        float currentScale = Easing.bounce(passedTime, 0.92F, 0.08F, 350L);
        float actualScale = Math.max(
                0.92F,
                this.lastScale + (currentScale - this.lastScale) * partialTicks
        );

        GL11.glTranslatef(sr.getScaledHeight(), sr.getScaledHeight() / 2.0F, 0.0F);
        GL11.glScalef(actualScale, actualScale, 0.0F);
        GL11.glTranslatef(-sr.getScaledHeight(), -sr.getScaledHeight() / 2.0F, 0.0F);

        this.lastScale = currentScale;
    }

    public float universalTransparency()
    {
        return this.universalTransparency;
    }

    public int lastMouseX()
    {
        return this.lastMouseX;
    }

    public int lastMouseY()
    {
        return this.lastMouseY;
    }

    public LinkedList<ModuleWindow> windows()
    {
        return this.windows;
    }

    public static RoseGui instance()
    {
        return instance;
    }
}
