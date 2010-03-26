package evopaint.gui.ruleeditor;

import evopaint.Selection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Observable;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 26.03.2010
 * Time: 12:53:45
 * To change this template use File | Settings | File Templates.
 */
public class SelectionList extends Observable implements Collection<Selection> {
    private ArrayList<Selection> selections = new ArrayList<Selection>();

    public boolean add(Selection selection) {
        boolean retVal = selections.add(selection);
        notifyObservers(selections);
        return retVal;
    }

    public boolean remove(Object o) {
        boolean retVal = selections.remove(o);
        notifyObservers(selections);
        return retVal;
    }

    public boolean containsAll(Collection<?> c) {
        return selections.containsAll(c);
    }

    public boolean addAll(Collection<? extends Selection> c) {
        return selections.addAll(c);
    }

    public boolean removeAll(Collection<?> c) {
        return selections.removeAll(c);
    }

    public boolean retainAll(Collection<?> c) {
        return selections.retainAll(c);
    }

    public void clear() {
        selections.clear();
        notifyObservers(selections);
    }

    public int size() {
        return selections.size();
    }

    public boolean isEmpty() {
        return selections.isEmpty();
    }

    public boolean contains(Object o) {
        return selections.contains(o);
    }

    public Iterator<Selection> iterator() {
        return selections.iterator();
    }

    public Object[] toArray() {
        return selections.toArray();
    }

    public <T> T[] toArray(T[] a) {
        return selections.toArray(a);
    }
}
