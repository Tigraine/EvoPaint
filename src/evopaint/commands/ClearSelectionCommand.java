/*
 *  Copyright (C) 2010 Daniel Hoelbling (http://www.tigraine.at)
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

import evopaint.gui.SelectionManager;

/*
 *
 * @author Daniel Hoelbling (http://www.tigraine.at)
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
