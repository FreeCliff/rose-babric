package me.ht9.rose.feature;

import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.GuiYesNo;

import java.util.function.Consumer;

public class GuiConfirm extends GuiYesNo
{
    private final Consumer<Boolean> callback;

    public GuiConfirm(GuiScreen guiScreen, String message1, String message2, String yes, String no, Consumer<Boolean> callback)
    {
        super(guiScreen, message1, message2, yes, no, -1);
        this.callback = callback;
    }

    @Override
    protected void actionPerformed(GuiButton guiButton)
    {
        this.callback.accept(guiButton.id == 0);
    }
}
