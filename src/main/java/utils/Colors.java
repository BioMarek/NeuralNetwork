package utils;

import java.awt.Color;
import java.awt.Graphics2D;

public enum Colors {
    LIGHT_GREY(new Color(210, 210, 210, Settings.alpha)),
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

    public static Color getColor(int ordinal) {
        return switch (ordinal) {
            case 0 -> LIGHT_GREY.getColor();
            case 1 -> RED.getColor();
            case 2 -> GREEN.getColor();
            case 3 -> BLUE.getColor();
            case 4 -> YELLOW.getColor();
            case 5 -> ORANGE.getColor();
            case 6 -> VIOLET.getColor();
            case 7 -> MAGENTA.getColor();
            case 8 -> CYAN.getColor();
            case 9 -> PINK.getColor();
            case 10 -> BROWN.getColor();
            case 11 -> LIGHT_GREEN.getColor();
            case 12 -> LIGHT_BLUE.getColor();
            case 13 -> OLIVE.getColor();
            case 14 -> NAVY.getColor();
            case 15 -> TEAL.getColor();
            case 16 -> CORAL.getColor();
            case 17 -> KHAKI.getColor();
            case 18 -> new Color(255, 102, 102);
            case 19 -> new Color(153, 255, 51);
            case 20 -> new Color(204, 204, 0);
            case 21 -> new Color(204, 102, 0);
            case 22 -> new Color(102, 255, 255);
            case 23 -> new Color(0, 102, 204);
            case 24 -> new Color(204, 153, 255);
            case 25 -> new Color(255, 153, 255);
            case 26 -> new Color(153, 0, 76);
            case 27 -> new Color(0, 102, 102);
            case -1 -> BACKGROUND.getColor();
            default -> throw new RuntimeException("too many colors used");
        };
    }

    public static void setColor(Graphics2D graphics, int color) {
        graphics.setColor(getColor(color));
    }
}
