package me.ht9.rose.feature.gui;

import net.minecraft.src.ChatAllowedCharacters;
import net.minecraft.src.FontRenderer;
import net.minecraft.src.GuiScreen;

public class GuiTextField extends net.minecraft.src.GuiTextField
{
    private final GuiScreen parentGuiScreen;
    private int maxStringLength;

    public GuiTextField(GuiScreen guiScreen, FontRenderer fontRenderer, int i, int j, int k, int l, String string)
    {
        super(guiScreen, fontRenderer, i, j, k, l, string);
        this.parentGuiScreen = guiScreen;
    }

    @Override
    public void setMaxStringLength(int i)
    {
        super.setMaxStringLength(i);
        maxStringLength = i;
    }

    @Override
    public void textboxKeyTyped(char c, int i)
    {
        if (this.isEnabled && this.isFocused) {
            if (c == '\t') {
                this.parentGuiScreen.selectNextField();
            }

            String text = this.getText();

            if (c == 22) {
                String var3 = GuiScreen.getClipboardString();
                if (var3 == null) {
                    var3 = "";
                }

                int var4 = this.maxStringLength - text.length();
                if (var4 > var3.length()) {
                    var4 = var3.length();
                }

                if (var4 > 0) {
                    text = text + var3.substring(0, var4);
                    this.setText(text);
                }
            }

            if (i == 14 && !text.isEmpty()) {
                text = text.substring(0, text.length() - 1);
                this.setText(text);
            }

            if (ChatAllowedCharacters.allowedCharacters.indexOf(c) >= 0 && (text.length() < this.maxStringLength || this.maxStringLength == 0)) {
                text = text + c;
                this.setText(text);
            }
        }
    }
}
