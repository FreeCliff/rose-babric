package me.ht9.rose.util.render.font;

import me.ht9.rose.Rose;
import me.ht9.rose.util.Globals;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import static org.lwjgl.opengl.GL11.*;

public abstract class CFont implements Globals
{
    protected final CharData[] charData = new CharData[256];
    protected Font font;
    protected boolean antiAlias;
    protected boolean fractionalMetrics;
    protected int fontHeight = -1;
    protected final int charOffset = 0;
    protected int textureID;

    public CFont(Font font, boolean antiAlias, boolean fractionalMetrics)
    {
        this.font = font;
        this.antiAlias = antiAlias;
        this.fractionalMetrics = fractionalMetrics;
        this.textureID = this.setupTexture(font, antiAlias, fractionalMetrics, this.charData);
    }

    protected int setupTexture(Font font, boolean antiAlias, boolean fractionalMetrics, CharData[] chars)
    {
        BufferedImage image = this.generateFontImage(font, antiAlias, fractionalMetrics, chars);
        try
        {
            int id = glGenTextures();
            mc.renderEngine.setupTexture(image, id);
            return id;
        }
        catch (Exception e)
        {
            Rose.logger().error("Error occurred while trying to set up texture", e);
            return -1;
        }
    }

    protected BufferedImage generateFontImage(Font font, boolean antiAlias, boolean fractionalMetrics, CharData[] chars)
    {
        int imgSize = 512;

        BufferedImage bufferedImage = new BufferedImage(imgSize, imgSize, 2);
        Graphics2D g = (Graphics2D) bufferedImage.getGraphics();
        g.setFont(font);
        g.setColor(new Color(255, 255, 255, 0));
        g.fillRect(0, 0, imgSize, imgSize);
        g.setColor(Color.WHITE);
        g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, fractionalMetrics ? RenderingHints.VALUE_FRACTIONALMETRICS_ON : RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, antiAlias ? RenderingHints.VALUE_TEXT_ANTIALIAS_ON : RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, antiAlias ? RenderingHints.VALUE_ANTIALIAS_ON : RenderingHints.VALUE_ANTIALIAS_OFF);

        FontMetrics fontMetrics = g.getFontMetrics();
        int charHeight = 0;
        int positionX = 0;
        int positionY = 1;
        for (int i = 0; i < chars.length; i++)
        {
            char ch = (char) i;

            CharData charData = new CharData();
            Rectangle2D dimensions = fontMetrics.getStringBounds(String.valueOf(ch), g);
            charData.width = dimensions.getBounds().width + 8;
            charData.height = dimensions.getBounds().height;
            if (positionX + charData.width >= imgSize)
            {
                positionX = 0;
                positionY += charHeight;
                charHeight = 0;
            }
            if (charData.height > charHeight)
            {
                charHeight = charData.height;
            }
            charData.storedX = positionX;
            charData.storedY = positionY;
            if (charData.height > this.fontHeight)
            {
                this.fontHeight = charData.height;
            }
            chars[i] = charData;
            g.drawString(String.valueOf(ch), positionX + 2, positionY + fontMetrics.getAscent());
            positionX += charData.width;
        }
        return bufferedImage;
    }

    @SuppressWarnings("ClassEscapesDefinedScope")
    public void drawChar(CharData[] chars, char c, float x, float y) throws ArrayIndexOutOfBoundsException
    {
        try
        {
            this.drawQuad(x, y, chars[c].width, chars[c].height, chars[c].storedX, chars[c].storedY, chars[c].width, chars[c].height);
        }
        catch (Throwable t)
        {
            Rose.logger().error("Error occurred while drawing character", t);
        }
    }

    protected void drawQuad(float x, float y, float width, float height, float srcX, float srcY, float srcWidth, float srcHeight)
    {
        float renderSRCX = srcX / 512.0f;
        float renderSRCY = srcY / 512.0f;
        float renderSRCWidth = srcWidth / 512.0f;
        float renderSRCHeight = srcHeight / 512.0f;

        glTexCoord2f(renderSRCX + renderSRCWidth, renderSRCY);
        glVertex2d(x + width, y);
        glTexCoord2f(renderSRCX, renderSRCY);
        glVertex2d(x, y);
        glTexCoord2f(renderSRCX, renderSRCY + renderSRCHeight);
        glVertex2d(x, y + height);
        glTexCoord2f(renderSRCX, renderSRCY + renderSRCHeight);
        glVertex2d(x, y + height);
        glTexCoord2f(renderSRCX + renderSRCWidth, renderSRCY + renderSRCHeight);
        glVertex2d(x + width, y + height);
        glTexCoord2f(renderSRCX + renderSRCWidth, renderSRCY);
        glVertex2d(x + width, y);
    }

    public int getStringHeight(String text)
    {
        return this.getHeight();
    }

    public int getHeight()
    {
        return (this.fontHeight - 8) / 2;
    }

    public int getStringWidth(String text)
    {
        int width = 0;
        for (char c : text.toCharArray())
        {
            if (c >= this.charData.length) continue;
            width += this.charData[c].width - 8 + this.charOffset;
        }
        return width / 2;
    }

    public boolean isAntiAlias()
    {
        return this.antiAlias;
    }

    public void setAntiAlias(boolean antiAlias)
    {
        if (this.antiAlias != antiAlias)
        {
            this.antiAlias = antiAlias;
            this.textureID = this.setupTexture(this.font, antiAlias, this.fractionalMetrics, this.charData);
        }
    }

    public boolean isFractionalMetrics()
    {
        return this.fractionalMetrics;
    }

    public void setFractionalMetrics(boolean fractionalMetrics)
    {
        if (this.fractionalMetrics != fractionalMetrics)
        {
            this.fractionalMetrics = fractionalMetrics;
            this.textureID = this.setupTexture(this.font, this.antiAlias, fractionalMetrics, this.charData);
        }
    }

    public Font getFont()
    {
        return font;
    }

    public void setFont(Font font)
    {
        this.font = font;
        this.textureID = this.setupTexture(font, this.antiAlias, this.fractionalMetrics, this.charData);
    }

    protected static class CharData
    {
        public int width;
        public int height;
        public int storedX;
        public int storedY;
    }
}
