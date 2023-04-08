package utils;

import java.awt.Color;

public enum Colors {
    LIGHT_GREY(new Color(210, 210, 210,Settings.alpha)),
    RED(new Color(255, 0, 0, Settings.alpha)),
    GREEN(new Color(0, 255, 0, Settings.alpha)),
    BLUE(new Color(0, 0, 255, Settings.alpha)),
    YELLOW(new Color(255, 255, 0, Settings.alpha)),
    ORANGE(new Color(255, 200, 0, Settings.alpha)),
    MAGENTA(new Color(255, 0, 255, Settings.alpha)),
    CYAN(new Color(0, 255, 255, Settings.alpha)),
    PINK(new Color(255, 175, 175, Settings.alpha)),

    VIOLET(new Color(127, 0, 255, Settings.alpha)),
    BROWN(new Color(139, 69, 19, Settings.alpha)),
    LIGHT_GREEN(new Color(128, 255, 0, Settings.alpha)),
    LIGHT_BLUE(new Color(0, 128, 255, Settings.alpha)),
    OLIVE(new Color(128, 128, 0, Settings.alpha)),
    NAVY(new Color(0, 0, 128, Settings.alpha)),
    TEAL(new Color(0, 128, 128, Settings.alpha)),
    CORAL(new Color(240, 128, 128, Settings.alpha)),
    KHAKI(new Color(240, 232, 170, Settings.alpha)),
    BACKGROUND(new Color(30, 30, 30, 255)),

    TEXT(new Color(215, 215, 215, 255));

    private final Color color;

    Colors(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return this.color;
    }
}
