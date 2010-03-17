/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.util;

import evopaint.interfaces.IRandomNumberGenerator;
import java.util.AbstractCollection;
import java.util.Iterator;

/**
 *
 * @author tam
 */

public class ParallaxMap<T> extends AbstractCollection<T> {
    private class ParallaxMapIterator<T> implements Iterator {

        T [] array;
        int i;

        public boolean hasNext() {
            if (i == array.length) {
                    return false;
            }
            while (array[i] == null) {
                i++;
                if (i == array.length) {
                    return false;
                }
            }
            return true;
        }

        public T next() {
            return array[i++];
        }

        public void remove() {
            array[i] = null;
        }

        public ParallaxMapIterator(T [] array) {
            this.i = 0;
            this.array = array;
        }
    }

    private T [] array;
    protected int width;
    protected int height;
    //protected int size;

    @Override
    public int size() {
        //return size;
        return array == null ? 0 : array.length;
    }

    @Override
    public Iterator<T> iterator() {
        return new ParallaxMapIterator(array);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public T get(int x, int y) {
        return array[clamp(y, height) * width + clamp(x, width)];
    }

    public T getRandom(IRandomNumberGenerator rng) {
        int rnd = rng.nextPositiveInt(array.length);
        int i = rnd;
        while (array[i] == null) {
            i++;
            if (i == array.length) {
                i = 0;
            }
            if (i == rnd) {
                Logger.log.warning("calling getRandom() on empty ParallaxMap", (Object[])null);
            }
        }
        return array[i];
    }

    public void set(int x, int y, T object) {
        /*
        int i = clamp(y, height) * width + clamp(x, width);
        if (array[i] == null) {
            if (object != null) {
                size++;
            }
        } else if (object == null) {
                size--;
        }
        array[i] = object;
        */
        array[clamp(y, height) * width + clamp(x, width)] = object;
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
        this.array = array;
        this.width = width;
        this.height = height;
    }
}

