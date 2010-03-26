package evopaint.commands;

import evopaint.gui.SelectionManager;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 26.03.2010
 * Time: 13:50:44
 * To change this template use File | Settings | File Templates.
 */
public class DeleteCurrentSelectionCommand extends AbstractCommand {
    private SelectionManager selectionManager;

    public DeleteCurrentSelectionCommand(SelectionManager selectionManager) {
        this.selectionManager = selectionManager;
    }

    public void execute() {
        selectionManager.removeActiveSelection();
    }
}
