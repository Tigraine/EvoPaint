package evopaint.commands;

import evopaint.Config;
import evopaint.entities.Selection;
import evopaint.gui.SelectionReceiver;
import evopaint.util.Logger;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 07.03.2010
 * Time: 12:13:56
 * To change this template use File | Settings | File Templates.
 */
public class SelectCommand extends AbstractCommand {
    private SelectionReceiver receiver;

    public enum State { IDLE, STARTED }
    private State CurrentState = State.IDLE;

    private Point mouseLocation;

    public SelectCommand(SelectionReceiver receiver){

        this.receiver = receiver;
    }

    public void setLocation(Point location){
        mouseLocation = location;
    }

    private Point startPoint;
    private Point endPoint;

    public void execute() {
        Logger.log.error("Selection pressed");
        if (CurrentState == State.IDLE){
            startPoint = mouseLocation;
            CurrentState = State.STARTED;
        } else if (CurrentState == State.STARTED) {
            endPoint = mouseLocation;
            CurrentState = State.IDLE;
            receiver.setSelection(new Selection(startPoint, endPoint));
        }
    }
}
