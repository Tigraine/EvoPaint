package evopaint.gui;

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

        notifyOfChange(ChangeType.ITEM_ADDED, selection);
        return retVal;
    }

    public boolean remove(Object o) {
        boolean retVal = selections.remove(o);
        notifyOfChange(ChangeType.ITEM_DELETED, o);
        return retVal;
    }

    private void notifyOfChange(ChangeType type, Object o) {
        this.setChanged();
        notifyObservers(new SelectionListEventArgs(type, (Selection)o));
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
        notifyOfChange(ChangeType.LIST_CLEARED, null);
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

    public enum ChangeType { ITEM_ADDED, ITEM_DELETED, LIST_CLEARED };

    public class SelectionListEventArgs {
        private ChangeType changeType;
        private Selection selection;

        public ChangeType getChangeType() {
            return changeType;
        }

        public SelectionListEventArgs(ChangeType changeType, Selection selection) {
            this.changeType = changeType;
            this.selection = selection;
        }

        public Selection getSelection() {
            return selection;
        }
    }
}
