package utils;

import java.awt.Color;
import java.awt.Graphics2D;

public enum Colors {
    BACKGROUND(new Color(30, 30, 30, 255)),
    TEXT(new Color(215, 215, 215, 255));

    private final Color color;

    Colors(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return this.color;
    }

    public static Color getColor(int ordinal, int alpha) {
        return switch (ordinal) {
            case 1 -> new Color(210, 210, 210, alpha);
            case 2 -> new Color(255, 0, 0, alpha);
            case 3 -> new Color(0, 255, 0, alpha);
            case 4 -> new Color(0, 0, 255, alpha);
            case 5 -> new Color(255, 255, 0, alpha);
            case 6 -> new Color(255, 200, 0, alpha);
            case 7 -> new Color(255, 0, 255, alpha);
            case 8 -> new Color(127, 0, 255, alpha);
            case 9 -> new Color(0, 255, 255, alpha);
            case 10 -> new Color(255, 175, 175, alpha);
            case 11 -> new Color(139, 69, 19, alpha);
            case 12 -> new Color(0, 128, 255, alpha);
            case 13 -> new Color(128, 128, 0, alpha);
            case 14 -> new Color(0, 0, 128, alpha);
            case 15 -> new Color(0, 128, 128, alpha);
            case 16 -> new Color(240, 128, 128, alpha);
            case 17 -> new Color(240, 232, 170, alpha);
            case 18 -> new Color(128, 255, 0, alpha);
            case -1 -> new Color(30, 30, 30, 255);
            default -> throw new RuntimeException("too many colors used");
        };
    }

    public static void setColor(Graphics2D graphics, int color, int alpha) {
        graphics.setColor(getColor(color, alpha));
    }
}
