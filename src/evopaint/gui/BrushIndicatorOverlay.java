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

package evopaint.gui;

import evopaint.gui.util.IOverlay;
import evopaint.gui.util.WrappingScalableCanvas;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 *
 * @author Markus Echterhoff <tam@edu.uni-klu.ac.at>
 */
public class BrushIndicatorOverlay extends Rectangle implements IOverlay {
    private WrappingScalableCanvas canvas;

    @Override
    public void setBounds(Rectangle bounds) {
        super.setBounds(bounds);
        this.x -= bounds.width / 2;
        this.y -= bounds.height / 2;
    }

    public BrushIndicatorOverlay(WrappingScalableCanvas canvas, Rectangle bounds) {
        super(bounds);
        this.canvas = canvas;
    }

    public void paint() {
        canvas.paintOverlay(this, Color.WHITE, .5f);
    }
}
