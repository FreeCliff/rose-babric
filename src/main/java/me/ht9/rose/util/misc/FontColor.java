package me.ht9.rose.util.misc;

@SuppressWarnings("unused")
public enum FontColor
{
    BLACK(0x0),
    DARK_BLUE(0x1),
    DARK_GREEN(0x2),
    DARK_AQUA(0x3),
    DARK_RED(0x4),
    DARK_PURPLE(0x5),
    GOLD(0x6),
    GRAY(0x7),
    DARK_GRAY(0x8),
    BLUE(0x9),
    GREEN(0xA),
    AQUA(0xB),
    RED(0xC),
    LIGHT_PURPLE(0xD),
    YELLOW(0xE),
    WHITE(0xF);

    private final int code;

    FontColor(int code)
    {
        this.code = code;
    }

    @Override
    public String toString()
    {
        return String.format("§%x", code);
    }


}
