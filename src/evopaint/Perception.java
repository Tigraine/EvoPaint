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

package evopaint;

import evopaint.pixel.Pixel;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

/**
 *
 * @author Markus Echterhoff <tam@edu.uni-klu.ac.at>
 */
public class Perception {
    private BufferedImage image;
    private int[] internalImage;

    public BufferedImage getImage() {
        return image;
    }

    public void createImage(World world) {
        for (int i = 0; i < internalImage.length; i++) {
            Pixel pixie = world.getNotSynchronized(i);
            internalImage[i] =
                        pixie == null ?
                        world.getConfiguration().backgroundColor :
                        pixie.getPixelColor().getInteger();
        }
    }

    public Perception(BufferedImage image) {
        this.image = image;
        this.internalImage = ((DataBufferInt)this.image.getRaster().getDataBuffer()).getData();

    }
}
