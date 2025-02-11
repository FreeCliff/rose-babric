package me.ht9.rose.feature.gui.font;

import me.ht9.rose.feature.module.modules.client.hud.Hud;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public final class CFontRenderer extends CFont
{
    private final CharData[] boldChars = new CharData[256];
    private final CharData[] italicChars = new CharData[256];
    private final CharData[] boldItalicChars = new CharData[256];
    private final int[] colorCode = new int[32];
    private int textureIDBold;
    private int textureIDItalic;
    private int textureIDItalicBold;

    public CFontRenderer(Font font)
    {
        this(font, true, true);
    }

    public CFontRenderer(Font font, boolean antiAlias, boolean fractionalMetrics)
    {
        super(font, antiAlias, fractionalMetrics);
        this.setupMinecraftColorcodes();
        this.setupBoldItalicIDs();
    }

    public float drawCenteredStringWithShadow(String text, float x, float y, int color)
    {
        return this.drawStringWithShadow(text, x - (float)(this.getStringWidth(text) / 2), y, color);
    }

    public float drawCenteredString(String text, float x, float y, int color)
    {
        return this.drawString(text, x - (float)(this.getStringWidth(text) / 2), y, color);
    }

    public float drawStringWithShadow(String text, double x, double y, int color)
    {
        float shadowWidth = this.drawString(text, x + 1.0, y, color, true);
        return Math.max(shadowWidth, this.drawString(text, x, y - 1.0, color, false));
    }

    public float drawString(String text, float x, float y, int color)
    {
        return this.drawString(text, x, y, color, false);
    }

    public float drawString(String text, double x, double y, int color, boolean shadow)
    {
        x -= 1.0;
        y -= 2.0;

        if (text == null) return 0.0f;

        if (color == 0x20FFFFFF) color = 0xFFFFFF;
        if ((color & 0xFC000000) == 0) color |= 0xFF000000;
        if (shadow) color = (color & 0xFCFCFC) >> 2 | color & 0xFF000000;

        CharData[] currentData = this.charData;
        float alpha = (float)(color >> 24 & 0xFF) / 255.0f;
        boolean bold = false;
        boolean italic = false;
        boolean strikethrough = false;
        boolean underline = false;

        x *= 2.0;
        y *= 2.0;

        glPushMatrix();
        glScaled(0.5, 0.5, 0.5);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glColor4f((float)(color >> 16 & 0xFF) / 255.0f, (float)(color >> 8 & 0xFF) / 255.0f, (float)(color & 0xFF) / 255.0f, alpha);

        int size = text.length();
        glEnable(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, textureID);

        for (int i = 0; i < size; i++)
        {
            char character = text.charAt(i);
            if (character == '§')
            {
                int colorIndex = 21;
                try
                {
                    colorIndex = "0123456789abcdefklmnor".indexOf(text.charAt(i + 1));
                }
                catch (Exception ignored) {}

                if (colorIndex < 16)
                {
                    bold = false;
                    italic = false;
                    underline = false;
                    strikethrough = false;
                    glBindTexture(GL_TEXTURE_2D, textureID);
                    currentData = this.charData;
                    if (colorIndex < 0) colorIndex = 15;
                    if (shadow) colorIndex += 16;
                    int colorcode = this.colorCode[colorIndex];
                    glColor4f((float)(colorcode >> 16 & 0xFF) / 255.0f, (float)(colorcode >> 8 & 0xFF) / 255.0f, (float)(colorcode & 0xFF) / 255.0f, alpha);
                }
                else if (colorIndex == 17)
                {
                    bold = true;
                    if (italic)
                    {
                        glBindTexture(GL_TEXTURE_2D, textureIDItalicBold);
                        currentData = this.boldItalicChars;
                    }
                    else
                    {
                        glBindTexture(GL_TEXTURE_2D, textureIDBold);
                        currentData = this.boldChars;
                    }
                }
                else if (colorIndex == 18)
                {
                    strikethrough = true;
                }
                else if (colorIndex == 19)
                {
                    underline = true;
                }
                else if (colorIndex == 20)
                {
                    italic = true;
                    if (bold)
                    {
                        glBindTexture(GL_TEXTURE_2D, textureIDItalicBold);
                        currentData = this.boldItalicChars;
                    }
                    else
                    {
                        glBindTexture(GL_TEXTURE_2D, textureIDItalic);
                        currentData = this.italicChars;
                    }
                }
                else if (colorIndex == 21)
                {
                    bold = false;
                    italic = false;
                    underline = false;
                    strikethrough = false;
                    glColor4f((float)(color >> 16 & 0xFF) / 255.0f, (float)(color >> 8 & 0xFF) / 255.0f, (float)(color & 0xFF) / 255.0f, alpha);
                    glBindTexture(GL_TEXTURE_2D, textureID);
                    currentData = this.charData;
                }
                i++;
                continue;
            }
            if (character >= currentData.length) continue;
            glBegin(GL_TRIANGLES);
            this.drawChar(currentData, character, (float) x, (float) y);
            glEnd();

            if (strikethrough)
                this.drawLine(x, y + (double)(currentData[character].height / 2), x + (double) currentData[character].width - 8.0, y + (double)(currentData[character].height / 2));

            if (underline)
                this.drawLine(x, y + (double)currentData[character].height - 2.0, x + (double) currentData[character].width - 8.0, y + (double)currentData[character].height - 2.0);

            x += currentData[character].width - 8 + this.charOffset;
        }
        glHint(GL_POLYGON_SMOOTH_HINT, GL_DONT_CARE);
        glPopMatrix();

        return (float) x / 2.0f;
    }

    public void drawGradientString(String text, double x, double y, boolean shadow)
    {
        x -= 1.0;
        y -= 2.0;

        int color = Hud.instance().getColor((float) x).getRGB();

        if (text == null) return;

        if (color == 0x20FFFFFF) color = 0xFFFFFF;
        if ((color & 0xFC000000) == 0) color |= 0xFF000000;
        if (shadow) color = (color & 0xFCFCFC) >> 2 | color & 0xFF000000;

        CharData[] currentData = this.charData;
        float alpha = (float)(color >> 24 & 0xFF) / 255.0f;
        boolean bold = false;
        boolean italic = false;
        boolean strikethrough = false;
        boolean underline = false;

        x *= 2.0;
        y *= 2.0;

        glPushMatrix();
        glScaled(0.5, 0.5, 0.5);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glColor4f((float)(color >> 16 & 0xFF) / 255.0f, (float)(color >> 8 & 0xFF) / 255.0f, (float)(color & 0xFF) / 255.0f, alpha);

        int size = text.length();
        glEnable(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, textureID);

        for (int i = 0; i < size; i++)
        {
            char character = text.charAt(i);
            if (!shadow)
            {
                color = Hud.instance().getColor((float) x / 2.0f).getRGB();
                glColor4f((float)(color >> 16 & 0xFF) / 255.0f, (float)(color >> 8 & 0xFF) / 255.0f, (float)(color & 0xFF) / 255.0f, alpha);
            }
            if (character == '§')
            {
                int colorIndex = 21;
                try
                {
                    colorIndex = "0123456789abcdefklmnor".indexOf(text.charAt(i + 1));
                }
                catch (Exception ignored) {}

                if (colorIndex < 16)
                {
                    bold = false;
                    italic = false;
                    underline = false;
                    strikethrough = false;
                    glBindTexture(GL_TEXTURE_2D, textureID);
                    currentData = this.charData;
                    if (colorIndex < 0) colorIndex = 15;
                    if (shadow) colorIndex += 16;
                    int colorcode = this.colorCode[colorIndex];
                    glColor4f((float)(colorcode >> 16 & 0xFF) / 255.0f, (float)(colorcode >> 8 & 0xFF) / 255.0f, (float)(colorcode & 0xFF) / 255.0f, alpha);
                }
                else if (colorIndex == 17)
                {
                    bold = true;
                    if (italic)
                    {
                        glBindTexture(GL_TEXTURE_2D, textureIDItalicBold);
                        currentData = this.boldItalicChars;
                    }
                    else
                    {
                        glBindTexture(GL_TEXTURE_2D, textureIDBold);
                        currentData = this.boldChars;
                    }
                }
                else if (colorIndex == 18)
                {
                    strikethrough = true;
                }
                else if (colorIndex == 19)
                {
                    underline = true;
                }
                else if (colorIndex == 20)
                {
                    italic = true;
                    if (bold)
                    {
                        glBindTexture(GL_TEXTURE_2D, textureIDItalicBold);
                        currentData = this.boldItalicChars;
                    }
                    else
                    {
                        glBindTexture(GL_TEXTURE_2D, textureIDItalic);
                        currentData = this.italicChars;
                    }
                }
                else if (colorIndex == 21)
                {
                    bold = false;
                    italic = false;
                    underline = false;
                    strikethrough = false;
                    glColor4f((float)(color >> 16 & 0xFF) / 255.0f, (float)(color >> 8 & 0xFF) / 255.0f, (float)(color & 0xFF) / 255.0f, alpha);
                    glBindTexture(GL_TEXTURE_2D, textureID);
                    currentData = this.charData;
                }
                i++;
                continue;
            }
            if (character >= currentData.length) continue;
            glBegin(GL_TRIANGLES);
            this.drawChar(currentData, character, (float) x, (float) y);
            glEnd();

            if (strikethrough)
                this.drawLine(x, y + (double)(currentData[character].height / 2), x + (double) currentData[character].width - 8.0, y + (double)(currentData[character].height / 2));

            if (underline)
                this.drawLine(x, y + (double)currentData[character].height - 2.0, x + (double) currentData[character].width - 8.0, y + (double)currentData[character].height - 2.0);

            x += currentData[character].width - 8 + this.charOffset;
        }
        glHint(GL_POLYGON_SMOOTH_HINT, GL_DONT_CARE);
        glPopMatrix();

    }

    @Override
    public int getStringWidth(String text) {
        if (text == null) return 0;

        int width = 0;
        CharData[] currentData = this.charData;
        int size = text.length();

        for (int i = 0; i < size; i++)
        {
            char character = text.charAt(i);
            if (character == '§')
            {
                i++;
                continue;
            }
            if (character >= currentData.length) continue;
            width += currentData[character].width - 8 + this.charOffset;
        }
        return width / 2;
    }

    @Override
    public void setFont(Font font) {
        super.setFont(font);
        this.setupBoldItalicIDs();
    }

    @Override
    public void setAntiAlias(boolean antiAlias) {
        super.setAntiAlias(antiAlias);
        this.setupBoldItalicIDs();
    }

    @Override
    public void setFractionalMetrics(boolean fractionalMetrics) {
        super.setFractionalMetrics(fractionalMetrics);
        this.setupBoldItalicIDs();
    }

    public List<String> wrapWords(String text, double width)
    {
        ArrayList<String> finalWords = new ArrayList<>();
        if ((double) this.getStringWidth(text) > width)
        {
            String[] words = text.split(" ");
            String currentWord = "";
            char lastColorCode = '\uffff';
            for (String word : words)
            {
                for (int i = 0; i < word.length(); i++)
                {
                    char c = word.toCharArray()[i];
                    if (c != '§' || i >= word.length() - 1) continue;
                    lastColorCode = word.toCharArray()[i + 1];
                }
                StringBuilder stringBuilder = new StringBuilder();
                if ((double) this.getStringWidth(stringBuilder.append(currentWord).append(word).append(" ").toString()) < width)
                {
                    currentWord = currentWord + word + " ";
                    continue;
                }
                finalWords.add(currentWord);
                currentWord = "§" + lastColorCode + word + " ";
            }
            if (!currentWord.isEmpty())
            {
                if ((double) this.getStringWidth(currentWord) < width)
                {
                    finalWords.add("§" + lastColorCode + currentWord + " ");
                }
                else
                {
                    finalWords.addAll(this.formatString(currentWord, width));
                }
            }
        }
        else
        {
            finalWords.add(text);
        }
        return finalWords;
    }

    public List<String> formatString(String string, double width)
    {
        ArrayList<String> finalWords = new ArrayList<>();
        String currentWord = "";
        char lastColorCode = '\uffff';
        char[] chars = string.toCharArray();
        for (int i = 0; i < chars.length; ++i) {
            char c = chars[i];
            if (c == '§' && i < chars.length - 1) {
                lastColorCode = chars[i + 1];
            }
            StringBuilder stringBuilder = new StringBuilder();
            if ((double)this.getStringWidth(stringBuilder.append(currentWord).append(c).toString()) < width) {
                currentWord = currentWord + c;
                continue;
            }
            finalWords.add(currentWord);
            currentWord = "§" + lastColorCode + c;
        }
        if (!currentWord.isEmpty()) {
            finalWords.add(currentWord);
        }
        return finalWords;
    }

    private void setupBoldItalicIDs()
    {
        this.textureIDBold = this.setupTexture(this.font.deriveFont(Font.BOLD), this.antiAlias, this.fractionalMetrics, this.boldChars);
        this.textureIDItalic = this.setupTexture(this.font.deriveFont(Font.ITALIC), this.antiAlias, this.fractionalMetrics, this.boldChars);
        this.textureIDItalicBold = this.setupTexture(this.font.deriveFont(Font.BOLD | Font.ITALIC), this.antiAlias, this.fractionalMetrics, this.boldChars);
    }

    private void drawLine(double x1, double y1, double x2, double y2)
    {
        glDisable(GL_TEXTURE_2D);
        glLineWidth((float) 1.0);

        glBegin(GL_LINES);
        glVertex2d(x1, y1);
        glVertex2d(x2, y2);
        glEnd();

        glEnable(GL_TEXTURE_2D);
    }

    private void setupMinecraftColorcodes()
    {
        for (int i = 0; i < 32; ++i)
        {
            int offset = (i >> 3 & 1) * 85;
            int red = (i >> 2 & 1) * 170 + offset;
            int green = (i >> 1 & 1) * 170 + offset;
            int blue = (i & 1) * 170 + offset;
            if (i == 6)
            {
                red += 85;
            }
            if (i >= 16)
            {
                red /= 4;
                green /= 4;
                blue /= 4;
            }
            this.colorCode[i] = (red & 0xff) << 16 | (green & 0xff) << 8 | blue & 0xff;
        }
    }
}
