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

package evopaint.util;

import evopaint.interfaces.IRandomNumberGenerator;
import java.awt.Dimension;
import java.awt.Point;
import java.util.Random;

/**
 * Random Number Generator class to switch to a better RNG, because we might
 * just need to. We preferably want real random numbers from the real world.
 * Eg. use /dev/random on UNIX systems "please move your mouse randomly to
 * generate the image"
 *
 * @author Markus Echterhoff <tam@edu.uni-klu.ac.at>
 */
public class RandomNumberGeneratorWrapper implements IRandomNumberGenerator {
    private Random rng;

    public RandomNumberGeneratorWrapper(Random rng) {
        this.rng = rng;
    }

    public Random getRNG() {
        return rng;
    }

    public int nextPositiveInt() {
        return this.rng.nextInt(Integer.MAX_VALUE); // modul to prevent negative values
    }

    public int nextPositiveInt(int modul) {
        return this.rng.nextInt(modul);
    }

    public double nextDouble() {
        return this.rng.nextDouble();
    }

    public float nextFloat() {
        return this.rng.nextFloat();
    }

    public boolean nextBoolean() {
        return this.rng.nextBoolean();
    }

    public Point nextLocation(Dimension dimension) {
        return new Point(this.rng.nextInt(dimension.width),
                    this.rng.nextInt(dimension.height));
    }

    public Random getRandom() {
        return rng;
    }
}
