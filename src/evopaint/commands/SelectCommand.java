/*
 *  Copyright (C) 2010 Daniel Hölbling
 *
 *  This file is part of EvoPaint.
 *
 *  EvoPaint is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with EvoPaint.  If not, see <http://www.gnu.org/licenses/>.
 */

package evopaint.commands;

import evopaint.Selection;
import evopaint.gui.SelectionList;

import java.awt.*;

/*
 *
 * @author Daniel Hölbling
 */
public class SelectCommand extends AbstractCommand {
    private SelectionList observableSelectionList;

    public enum State { IDLE, STARTED }
    private State CurrentState = State.IDLE;

    private Point mouseLocation;

    public SelectCommand(SelectionList list){
        observableSelectionList = list;
    }

    public void setLocation(Point location, double scale){
        mouseLocation = TranslatePointToScale(location, scale);
    }

    public static Point TranslatePointToScale(Point location, double scale) {
        Point point = new Point((int)(location.x / scale), (int)(location.y / scale));
        return point;
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
