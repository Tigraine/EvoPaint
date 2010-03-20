package evopaint.gui.listeners;

import evopaint.gui.SelectionManager;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 20.03.2010
 * Time: 23:05:18
 * To change this template use File | Settings | File Templates.
 */
public class SelectionListenerFactory {
    private SelectionManager manager;

    public SelectionListenerFactory(SelectionManager manager) {
        this.manager = manager;
    }

    public SelectionSetNameListener CreateSelectionSetNameListener(){
        return new SelectionSetNameListener(manager);
    }
}
