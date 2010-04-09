/*
 *  Copyright (C) 2010 Markus Echterhoff <tam@edu.uni-klu.ac.at>
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

package evopaint.gui.util;

/**
 * Classes which implement this interface can subscribe to a Overlayable
 * component
 * 
 * @author Markus Echterhoff <tam@edu.uni-klu.ac.at>
 * @see IOverlayable
 */
public interface IOverlay {
    /**
     * A callback called by any overlayable components this overlay has
     * subscribed to when it is to paint itself onto the overlayable surface
     * @see IOverlayable
     */
    public void paint();
}
