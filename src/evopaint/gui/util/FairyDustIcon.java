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
