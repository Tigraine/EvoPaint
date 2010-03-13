/*
 * Random Number Generator class to switch to a better RNG, because we might
 * just need to. We preferably want real random numbers from the real world.
 * Eg. use /dev/random on UNIX systems "please move your mouse randomly to
 * generate the image"
 */
package evopaint;

import evopaint.interfaces.IRandomNumberGenerator;
import java.awt.Dimension;
import java.awt.Point;
import java.util.Random;

/**
 *
 * @author tam
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

    public short nextBias() {
        return (short)(this.rng.nextInt(Short.MAX_VALUE - 1) + 1);
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
