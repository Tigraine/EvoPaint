package evopaint.commands;

import evopaint.Selection;
import evopaint.gui.SelectionReceiver;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 07.03.2010
 * Time: 12:13:56
 * To change this template use File | Settings | File Templates.
 */
public class SelectCommand extends AbstractCommand {
    private ArrayList<SelectionReceiver> receivers = new ArrayList<SelectionReceiver>();

    public void addSelectionListener(SelectionReceiver receiver) {
        receivers.add(receiver);
    }

    public enum State { IDLE, STARTED }
    private State CurrentState = State.IDLE;

    private Point mouseLocation;

    public SelectCommand(SelectionReceiver receiver){
        addSelectionListener(receiver);
    }

    public void setLocation(Point location){
        mouseLocation = location;
    }

    private Point startPoint;
    private Point endPoint;

    private int nextSelectionId = 0;
    public void execute() {
        if (CurrentState == State.IDLE){
            startPoint = mouseLocation;
            CurrentState = State.STARTED;
        } else if (CurrentState == State.STARTED) {
            endPoint = mouseLocation;
            CurrentState = State.IDLE;
            Selection selection = new Selection(startPoint, endPoint);
            selection.setSelectionName("New Selection " + nextSelectionId);
            nextSelectionId++;
            signalReceivers(selection);
        }
    }

    private void signalReceivers(Selection selection){
        for(SelectionReceiver receiver : receivers){
            receiver.addSelection(selection);
        }
    }
}
