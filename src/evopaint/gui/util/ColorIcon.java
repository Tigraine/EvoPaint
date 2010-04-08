package evopaint.gui.util;

/**
 *
 * @author Markus Echterhoff <tam@edu.uni-klu.ac.at>
 */
import javax.swing.*;
import java.awt.*;

public class ColorIcon implements Icon {

    private int height;
    private int width;
    private Color color;

    public ColorIcon(int height, int width, Color color) {
        this.height = height;
        this.width = width;
        this.color = color;
    }

    public int getIconHeight() {
        return height;
    }

    public int getIconWidth() {
        return width;
    }

    public void paintIcon(Component c, Graphics g, int x, int y) {
        g.setColor(color);
        g.fillRect(x, y, width - 1, height - 1);

        g.setColor(Color.black);
        g.drawRect(x, y, width - 1, height - 1);
    }
}
