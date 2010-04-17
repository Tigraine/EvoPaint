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

import evopaint.Brush;
import evopaint.Configuration;
import evopaint.Selection;
import evopaint.gui.Showcase;
import evopaint.pixel.Pixel;
import evopaint.pixel.PixelColor;
import evopaint.pixel.rulebased.RuleBasedPixel;
import evopaint.pixel.rulebased.RuleSet;
import evopaint.util.mapping.AbsoluteCoordinate;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;

/*
 *
 * @author Daniel Hoelbling (http://www.tigraine.at)
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
	private Point location;

    public FillSelectionCommand(Showcase showcase) {
        this.showcase = showcase;
        this.configuration = showcase.getConfiguration();

        this.energy = configuration.startingEnergy;
        this.ruleSet = configuration.paint.getCurrentRuleSet();
    }
    
    public void setLocation(Point location) {
		this.location = location;
    }

    public void execute() {
        this.color = configuration.paint.getCurrentColor();
        this.selection = showcase.getActiveSelection();
        Rectangle rectangle;
        if (selection == null) {
        	rectangle = new Rectangle(0, 0, configuration.world.getWidth(), configuration.world.getHeight());
        } else {
        	rectangle = new Rectangle(selection.getStartPoint(), new Dimension(selection.getEndPoint().x - selection.getStartPoint().x, selection.getEndPoint().y - selection.getStartPoint().y));
        }
        
        
        if (rectangle.contains(location)) {
        	System.out.println("Filling inside rect");
        	
        	for (int x = rectangle.x; x < rectangle.x + rectangle.width; x++){
              for (int y = rectangle.y; y < rectangle.y + rectangle.height; y++){
                  if ((x % density) != 0) continue;
                  if ((y % density) != 0) continue;
                  RuleBasedPixel newPixel = createPixel(x, y);
              }
          }
        } else{
        	Point currentLoc = new Point();
        	for(int x = 0; x < configuration.world.getWidth(); x++) {
        		for(int y = 0; y < configuration.world.getHeight(); y++) {
        			currentLoc.setLocation(x, y);
        			if ((x % density != 0)) continue;
        			if ((y % density != 0)) continue;
        			if (!rectangle.contains(currentLoc)) {
        				RuleBasedPixel newPixel = createPixel(x, y);
        			}
        		}
        	}
        }
    }

	private RuleBasedPixel createPixel(int x, int y) {
		RuleBasedPixel newPixel = new RuleBasedPixel(configuration.paint.getCurrentColor(), new AbsoluteCoordinate(x, y, configuration.world), configuration.startingEnergy, configuration.paint.getCurrentRuleSet());
		  configuration.world.set(newPixel);
		return newPixel;
	}
}
