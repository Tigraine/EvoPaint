package evopaint.commands;

import evopaint.Selection;
import evopaint.gui.SelectionObserver;
import evopaint.gui.ruleeditor.SelectionList;

import java.awt.*;
import java.util.ArrayList;
import java.util.Observer;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 07.03.2010
 * Time: 12:13:56
 * To change this template use File | Settings | File Templates.
 */
public class SelectCommand extends AbstractCommand {
    private SelectionList observableSelectionList;

    public enum State { IDLE, STARTED }
    private State CurrentState = State.IDLE;

    private Point mouseLocation;

    public SelectCommand(SelectionList list){
        observableSelectionList = list;
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
            SwapPoints();
            Selection selection = new Selection(startPoint, endPoint);
            selection.setSelectionName("New Selection " + nextSelectionId);
            nextSelectionId++;
            observableSelectionList.add(selection);
        }
    }

    private void SwapPoints() {
        if (startPoint.x > endPoint.x || startPoint.y > endPoint.y ) {
            Point temp = endPoint;
            endPoint = startPoint;
            startPoint = temp;
        }
    }
}
