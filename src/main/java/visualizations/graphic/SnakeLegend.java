package visualizations.graphic;

import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;


public class SnakeLegend {
    public Graphics2D graphics;

    public void drawLegend() {
        switchAntiAliasing(graphics, true);
        drawInfo();
        switchAntiAliasing(graphics, false);
    }

    /**
     * Displays information about ant rule being animated and the number of steps the ant has made.
     */
    public void drawInfo() {
//        int ruleTextSize = hexAnt.ruleLength() > 17 ? (int) (fontUnit * 0.9) : fontUnit;
//        graphics.setColor(Colors.TEXT.getColor());
//        graphics.setFont(new Font("Arial", Font.BOLD, (int) (fontUnit * 1.1)));
//        graphics.drawString("Rule: ", Settings.LEGEND_START_X + fontUnit, fontUnit * 2);
//        graphics.drawString(hexAnt.rule.getAttributeString(ruleTextSize).getIterator(), Settings.LEGEND_START_X + (int) (fontUnit * 4.5), fontUnit * 2);
//        graphics.drawString("Steps: " + Util.numberFormatter(hexAnt.steps), Settings.LEGEND_START_X + fontUnit, fontUnit * 4);
//
//
//        graphics.setStroke(new BasicStroke(3f * Settings.HEX_MULTIPLIER));
//        graphics.drawLine(Settings.LEGEND_START_X, 0, Settings.LEGEND_START_X, Settings.BACKGROUND_HEIGHT);
    }

    /**
     * Switches anti-aliasing on or off.
     *
     * @param isOn whether AA should be turned on
     */
    public static void switchAntiAliasing(Graphics2D graphics, boolean isOn) {
        if (isOn) {
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        } else {
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
            graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        }
    }
}
