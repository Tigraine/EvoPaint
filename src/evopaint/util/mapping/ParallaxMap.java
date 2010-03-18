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
    //protected int size;

    @Override
    public int size() {
        //return size;
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

    public T get(int x, int y) {
        return data[clamp(y, height) * width + clamp(x, width)];
    }

    public T get(AbsoluteCoordinate ac) {
        return data[clamp(ac.y, height) * width + clamp(ac.x, width)];
    }

    @SuppressWarnings({"unchecked"})
    public T [] getEnvironment(int x, int y) {
        Object [] ret = new Object [9];
        int loc = y * width + x;
        for (int i = loc - 4, j = 0; i <= loc + 4; i++, j++) {
            ret[j] = data[i];
        }
        return (T[])ret;
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
        data[clamp(y, height) * width + clamp(x, width)] = object;
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

