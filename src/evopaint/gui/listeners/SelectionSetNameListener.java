package evopaint.gui.listeners;

import evopaint.gui.SelectionManager;

import javax.swing.*;
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
        String s = JOptionPane.showInputDialog("Please enter the new name for your selection");
        if (s!= null && s.length() > 0)
        {
            manager.getActiveSelection().setSelectionName(s);
        }
    }
}
