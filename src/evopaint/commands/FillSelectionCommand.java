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

import evopaint.Brush;
import evopaint.Configuration;
import evopaint.Selection;
import evopaint.gui.Showcase;
import evopaint.pixel.Pixel;
import evopaint.pixel.PixelColor;
import evopaint.pixel.rulebased.RuleBasedPixel;
import evopaint.pixel.rulebased.RuleSet;
import evopaint.util.mapping.AbsoluteCoordinate;

import java.awt.geom.AffineTransform;

/*
 *
 * @author Daniel Hölbling
 */
public class FillSelectionCommand extends AbstractCommand {
    private Configuration configuration;
    private double scale;
    private AffineTransform affineTransform;
    private Selection selection;
    private PixelColor color;
    private Brush brush;
    private int energy;
    private RuleSet ruleSet;
    private Showcase showcase;
    protected int density = 1;

    public FillSelectionCommand(Showcase showcase) {
        this.showcase = showcase;
        this.configuration = showcase.getConfiguration();
        // FIXME this.scale = showcase.getScale();
        // FIXME this.affineTransform = showcase.getAffineTransform();

        this.energy = configuration.startingEnergy;
        this.ruleSet = configuration.paint.getCurrentRuleSet();
    }

    public void execute() {
        this.color = configuration.paint.getCurrentColor();
        this.selection = showcase.getActiveSelection();
        for (int x = selection.getStartPoint().x; x < selection.getEndPoint().x; x++){
            for (int y = selection.getStartPoint().y; y < selection.getEndPoint().y; y++){
                if ((x % density) != 0) continue;
                if ((y % density) != 0) continue;
                Pixel newPixel = new RuleBasedPixel(new PixelColor(color), new AbsoluteCoordinate(x, y, configuration.world), energy, ruleSet);
                configuration.world.set(newPixel);
            }
        }

    }
}
