/*
 *  Copyright (C) 2010 Markus Echterhoff <tam@edu.uni-klu.ac.at>
 *
 *  This file is part of EvoPaint.
 *
 *  EvoPaint is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with EvoPaint.  If not, see <http://www.gnu.org/licenses/>.
 */

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
