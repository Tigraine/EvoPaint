package evopaint.commands;

import evopaint.Configuration;

import java.awt.event.ActionListener;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 20.03.2010
 * Time: 23:09:55
 * To change this template use File | Settings | File Templates.
 */
public class CommandFactory {
    private Configuration configuration;
    
    private SelectCommand selectionCommand;
    private MoveCommand moveCommand;

    public MoveCommand getMoveCommand() {
        if (moveCommand == null) {
            moveCommand = new MoveCommand(configuration);
        }
        return moveCommand;
    }

    public SelectCommand GetSelectCommand() {
        if (selectionCommand == null)
            selectionCommand = new SelectCommand();
        return selectionCommand;
    }

    public CommandFactory(Configuration configuration) {
        this.configuration = configuration;
    }
}
