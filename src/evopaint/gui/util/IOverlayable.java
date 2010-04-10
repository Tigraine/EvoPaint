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

import java.awt.Color;
import java.awt.Shape;

/**
 * Classes implementing this interface can subscribe/unsubscribe overlays which
 * are to be painted on a surface. The overlays make use of the paintOverlay
 * callback to do the actual painting.
 * 
 * @author Markus Echterhoff <tam@edu.uni-klu.ac.at>
 * @see IOverlay
 */
public interface IOverlayable {
    
    /**
     * Subscribes an overlay to be notified about painting events
     * @param overlay The overlay to subscribe
     * @see IOverlay
     */
    public void subscribe(IOverlay overlay);

    /**
     * Unsubscribes an overlay. It will not receive any more painting calls
     * @param overlay The overlay to unsubscribed
     * @see IOverlay
     */
    public void unsubscribe(IOverlay overlay);
}
