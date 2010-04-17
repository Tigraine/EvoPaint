/*
 *  Copyright (C) 2010 Markus Echterhoff <tam@edu.uni-klu.ac.at>,
 *                      Daniel Hoelbling (http://www.tigraine.at)
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


import evopaint.Configuration;
import evopaint.Selection;
import evopaint.gui.SelectionManager;
import evopaint.util.logging.Logger;


import java.awt.Point;
import java.awt.Rectangle;

/*
 *
 * @author Markus Echterhoff <tam@edu.uni-klu.ac.at>
 * @author Daniel Hoelbling (http://www.tigraine.at)
 */
public class PaintCommand extends AbstractCommand {
    private Configuration configuration;
    private Point location;
	private final SelectionManager selectionManager;

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
    }

    public PaintCommand(Configuration configuration, SelectionManager selectionManager) {
        this.configuration = configuration;
		this.selectionManager = selectionManager;
    }

    public void execute() {
        Selection activeSelection = selectionManager.getActiveSelection();
        if (activeSelection != null) {
			Rectangle rectangle = activeSelection.getRectangle();
			int brushSize = configuration.brush.size / 2;

			if (location.x - brushSize < rectangle.x)
				location.x = rectangle.x + brushSize;
			if (location.x + brushSize > rectangle.x + rectangle.width) 
				location.x = rectangle.x + rectangle.width - brushSize;
			if (location.y - brushSize < rectangle.y)
				location.y = rectangle.y + brushSize;
			if (location.y + brushSize > rectangle.y + rectangle.height)
				location.y = rectangle.y + rectangle.height - brushSize;
        }
        System.out.println("painting");
        Logger.log.information("Executing Paint command on x: %s y: %s", location.x, location.y);
        configuration.brush.paint(location.x, location.y);
    }
}

