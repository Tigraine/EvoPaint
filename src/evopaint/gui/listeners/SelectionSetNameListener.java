package evopaint.gui.listeners;

import evopaint.gui.SelectionManager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 20.03.2010
 * Time: 23:04:50
 * To change this template use File | Settings | File Templates.
 */
public class SelectionSetNameListener implements ActionListener {
    private SelectionManager manager;

    public SelectionSetNameListener(SelectionManager manager) {
        this.manager = manager;
    }

    public void actionPerformed(ActionEvent e) {
        String textFromWindow = "Bla bla";
        manager.getActiveSelection().setSelectionName("Bla bla");
    }
}
