/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.interfaces;

import java.awt.Dimension;
import java.awt.Point;
import java.util.Random;

/**
 *
 * @author daniel
 */
public interface IRandomNumberGenerator {
    int nextPositiveInt();
    int nextPositiveInt(int maxValue);
    short nextBias();
    double nextDouble();
    float nextFloat();
    boolean nextBoolean();
    Point nextLocation(Dimension dimension);
    Random getRandom();
}
