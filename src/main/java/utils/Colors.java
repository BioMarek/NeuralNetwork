package utils;

import java.awt.Color;
import java.awt.Graphics2D;

public enum Colors {
    BACKGROUND(new Color(30, 30, 30, 255)),
    TEXT(new Color(215, 215, 215, 255)),
    FOOD(new Color(255, 0, 0, 255)),
    WALL(new Color(210, 210, 210, 255));

    private final Color color;

    Colors(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return this.color;
    }

    public static Color getColor(int ordinal, int alpha) {
        return switch (ordinal) {
            case 0 -> new Color(0, 255, 0, alpha);
            case 1 -> new Color(0, 0, 255, alpha);
            case 2 -> new Color(255, 255, 0, alpha);
            case 3 -> new Color(255, 200, 0, alpha);
            case 4 -> new Color(255, 0, 255, alpha);
            case 5 -> new Color(127, 0, 255, alpha);
            case 6 -> new Color(0, 255, 255, alpha);
            case 7 -> new Color(255, 175, 175, alpha);
            case 8 -> new Color(139, 69, 19, alpha);
            case 9 -> new Color(0, 128, 255, alpha);
            case 10 -> new Color(128, 128, 0, alpha);
            case 11 -> new Color(0, 0, 128, alpha);
            case 12 -> new Color(0, 128, 128, alpha);
            case 13 -> new Color(240, 128, 128, alpha);
            case 14 -> new Color(240, 232, 170, alpha);
            case 15 -> new Color(128, 255, 0, alpha);
            case 16 -> new Color(255, 102, 102, alpha);
            case 17 -> new Color(153, 255, 51, alpha);
            case 18 -> new Color(204, 204, 0, alpha);
            case 19 -> new Color(204, 102, 0, alpha);
            case 20 -> new Color(102, 255, 255, alpha);
            case 21 -> new Color(0, 102, 204, alpha);
            case 22 -> new Color(204, 153, 255, alpha);
            case 23 -> new Color(255, 153, 255, alpha);
            case 24 -> new Color(153, 0, 76, alpha);
            case 25 -> new Color(0, 102, 102, alpha);
            case 26 -> new Color(30, 30, 30, 255);
            default -> throw new RuntimeException("too many colors used");
        };
    }

    public static void setColor(Graphics2D graphics, int color, int alpha) {
        graphics.setColor(getColor(color, alpha));
    }

    public static void setColor(Graphics2D graphics, Color color) {
        graphics.setColor(color);
    }

    public static Color textWithAlpha(int alpha) {
        return new Color(210, 210, 210, alpha);
    }

    public static Color redWithAlpha(int alpha) {
        return new Color(255, 0, 0, alpha);
    }
    public static Color lightGreenWithAlpha(int alpha) {
        return new Color(143, 238, 143, alpha);
    }
    public static Color lightLightVioletWithAlpha(int alpha) {
        return new Color(171, 102, 248, alpha);
    }

    public static Color wallWithAlpha(int alpha) {
        return new Color(210, 210, 210, alpha);
    }

    public static Color colorMixing(Color from, Color to, int stage) {
        if (stage <= 0) {
            return from;
        } else if (stage >= Settings.CHANGING_SLOW_FRAMES) {
            return to;
        } else {
            float ratio = (float) stage / Settings.CHANGING_SLOW_FRAMES;
            int red = (int) (from.getRed() + (to.getRed() - from.getRed()) * ratio);
            int green = (int) (from.getGreen() + (to.getGreen() - from.getGreen()) * ratio);
            int blue = (int) (from.getBlue() + (to.getBlue() - from.getBlue()) * ratio);
            int alpha = (int) (from.getAlpha() + (to.getAlpha() - from.getAlpha()) * ratio);
            return new Color(red, green, blue, alpha);
        }
    }
}
