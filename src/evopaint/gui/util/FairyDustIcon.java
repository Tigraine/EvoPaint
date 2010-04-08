package evopaint.gui.util;

/**
 *
 * @author Markus Echterhoff <tam@edu.uni-klu.ac.at>
 */
import evopaint.Configuration;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class FairyDustIcon extends ImageIcon {

    public FairyDustIcon(Configuration configuration, int height, int width) {
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = img.getGraphics();
        g.setColor(Color.black);
        g.drawRect(0, 0, width - 1, height - 1);
        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                g.setColor(new Color(configuration.rng.nextPositiveInt(0x01000000)));
                g.drawLine(x, y, x, y);
            }
        }
        setImage(img);
    }
}
