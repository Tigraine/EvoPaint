/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.util.mapping;

import evopaint.interfaces.IRandomNumberGenerator;
import evopaint.util.logging.Logger;
import java.util.AbstractCollection;
import java.util.Iterator;

/**
 *
 * @author tam
 */

public class ParallaxMap<T> extends AbstractCollection<T> {
    private class ParallaxMapIterator<T> implements Iterator {

        T [] data;
        int i;

        public boolean hasNext() {
            if (i == data.length) {
                    return false;
            }
            while (data[i] == null) {
                i++;
                if (i == data.length) {
                    return false;
                }
            }
            return true;
        }

        public T next() {
            return data[i++];
        }

        public void remove() {
            data[i] = null;
        }

        public ParallaxMapIterator(T [] array) {
            this.i = 0;
            this.data = array;
        }
    }

    private T [] data;
    protected int width;
    protected int height;
    private int nrElements;

    @Override
    public int size() {
        return data == null ? 0 : data.length;
    }

    @Override
    public Iterator<T> iterator() {
        return new ParallaxMapIterator(data);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public synchronized T  get(int i) {
        return data[i];
    }

    public synchronized T get(int x, int y) {
        return data[clamp(y, height) * width + clamp(x, width)];
    }

    public synchronized T get(AbsoluteCoordinate ac) {
        return data[clamp(ac.y, height) * width + clamp(ac.x, width)];
    }

    public synchronized T get(AbsoluteCoordinate ac, RelativeCoordinate rc) {
        assert(rc != RelativeCoordinate.ALL);
        return data[clamp(ac.y + rc.y, height) * width + clamp(ac.x + rc.x, width)];
    }

    public synchronized T [] getNeighborhood(AbsoluteCoordinate ac) {
        return getNeighborhood(ac.x, ac.y);
    }

    @SuppressWarnings({"unchecked"})
    public synchronized T [] getNeighborhood(int x, int y) {
        Object [] ret = new Object [9];
        int loc = y * width + x;
        for (int i = loc - 4, j = 0; i <= loc + 4; i++, j++) {
            ret[j] = data[i];
        }
        return (T[])ret;
    }

    public synchronized int [] getShuffledIndices(IRandomNumberGenerator rng) {
        int [] indices = new int[nrElements];

        for (int i = 0, ii = 0; ii < nrElements && i < data.length; i++) {
            if (data[i] != null) {
                indices[ii++] = i;
            }
        }
        
        // Durstenfeld's algorithm for permutating
        // http://en.wikipedia.org/wiki/Fisher-Yates_shuffle
        // n is the number of items remaining to be shuffled.
        for (int n = indices.length; n > 1; n--) {
            // Pick a random element to swap with the nth element.
            int k = rng.nextPositiveInt(n);  // 0 <= k <= n-1 (0-based array)
            // Swap array elements.
            int tmp = indices[k];
            indices[k] = indices[n-1];
            indices[n-1] = tmp;
        }

        return indices;
    }
 
    public T getRandom(IRandomNumberGenerator rng) {
        int rnd = rng.nextPositiveInt(data.length);
        int i = rnd;
        while (data[i] == null) {
            i++;
            if (i == data.length) {
                i = 0;
            }
            if (i == rnd) {
                Logger.log.warning("calling getRandom() on empty ParallaxMap", (Object[])null);
            }
        }
        return data[i];
    }

    public synchronized void set(int i, T object) {
        if (data[i] == null) {
            if (object != null) {
                nrElements++;
            }
        } else if (object == null) {
                nrElements--;
        }
        data[i] = object;
    }

    protected synchronized void set(int x, int y, T object) {
        int i = clamp(y, height) * width + clamp(x, width);
        set(i, object);
    }

    public static int clamp(int index, int length) {
        while (index < 0) {
            index += length;
        }
        while (index >= length) {
            index -= length;
        }
        return index;
    }

    public ParallaxMap(T[] array, int width, int height) {
        assert (array != null);
        assert (width > 0);
        assert (height > 0);
        this.data = array;
        this.width = width;
        this.height = height;
    }
}

