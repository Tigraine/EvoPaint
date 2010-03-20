package evopaint.commands;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 20.03.2010
 * Time: 23:09:55
 * To change this template use File | Settings | File Templates.
 */
public class CommandFactory {
    private SelectCommand selectionCommand;

    public SelectCommand GetSelectCommand() {
        if (selectionCommand == null)
            selectionCommand = new SelectCommand();
        return selectionCommand;
    }
}
