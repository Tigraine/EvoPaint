package evopaint.commands;

import evopaint.gui.SelectionManager;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 21.03.2010
 * Time: 11:49:12
 * To change this template use File | Settings | File Templates.
 */
public class ClearSelectionCommand extends AbstractCommand {
    private SelectionManager manager;

    public ClearSelectionCommand(SelectionManager manager) {
        this.manager = manager;
    }

    public void execute() {
        manager.clearSelections();
    }
}
