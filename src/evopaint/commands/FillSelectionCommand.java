/*
 *  Copyright (C) 2010 Daniel Hoelbling (http://www.tigraine.at),
 *                      Markus Echterhoff <tam@edu.uni-klu.ac.at>
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
import evopaint.Paint;
import evopaint.Selection;
import evopaint.gui.Showcase;
import evopaint.interfaces.IChangeListener;
import evopaint.pixel.PixelColor;
import evopaint.pixel.rulebased.Rule;
import evopaint.pixel.rulebased.RuleBasedPixel;
import evopaint.pixel.rulebased.RuleSet;
import evopaint.util.mapping.AbsoluteCoordinate;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

/*
 *
 * @author Daniel Hoelbling (http://www.tigraine.at)
 * @author Markus Echterhoff <tam@edu.uni-klu.ac.at>
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
    }

    public void setLocation(Point location) {
        this.location = location;
    }

    public void execute() {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                configuration.world.addChangeListener(new IChangeListener() {

                    public void changed() {
                        color = configuration.paint.getCurrentColor();
                        selection = showcase.getActiveSelection();
                        Rectangle rectangle;
                        if (selection == null) {
                            rectangle = new Rectangle(0, 0, configuration.world.getWidth(),
                                    configuration.world.getHeight());
                        } else {
                            rectangle = selection.getRectangle();
                        }

                        if (rectangle.contains(location)) {
                            //System.out.println("Filling inside rect " + rectangle);

                            for (int x = rectangle.x; x < rectangle.x + rectangle.width; x++) {
                                for (int y = rectangle.y; y < rectangle.y + rectangle.height; y++) {
                                    if ((x % density) != 0) {
                                        continue;
                                    }
                                    if ((y % density) != 0) {
                                        continue;
                                    }
                                    createPixel(x, y);
                                }
                            }
                        } else {
                            //System.out.println("Filling outside rect");
                            Point currentLoc = new Point();
                            for (int x = 0; x < configuration.world.getWidth(); x++) {
                                for (int y = 0; y < configuration.world.getHeight(); y++) {
                                    currentLoc.setLocation(x, y);
                                    if ((x % density != 0)) {
                                        continue;
                                    }
                                    if ((y % density != 0)) {
                                        continue;
                                    }
                                    if (!rectangle.contains(currentLoc)) {
                                        createPixel(x, y);
                                    }
                                }
                            }
                        }
                    }
                });
            }
        });

    }

    private void createPixel(int x, int y) {
        PixelColor currentColor = new PixelColor(configuration.paint.getCurrentColor());
        switch (configuration.paint.getCurrentColorMode()) {
            case Paint.FAIRY_DUST:
                currentColor.setInteger(configuration.rng.nextPositiveInt());
                break;
            case Paint.EXISTING_COLOR:
                RuleBasedPixel pixel = configuration.world.get(x, y);
                if (pixel == null) {
                    return;
                }
                currentColor.setColor(pixel.getPixelColor());
                break;
            default:
                break;
        }

        RuleSet currentRuleSet = configuration.paint.getCurrentRuleSet();
        switch (configuration.paint.getCurrentRuleSetMode()) {
            case Paint.NO_RULE_SET:
                currentRuleSet = null;
                break;
            case Paint.EXISTING_RULE_SET:
                RuleBasedPixel pixel = configuration.world.get(x, y);
                if (pixel == null || !(pixel instanceof RuleBasedPixel)) {
                    currentRuleSet = null;
                } else {
                    currentRuleSet = pixel.createRuleSet();
                }
                break;
            default:
                break;
        }
        AbsoluteCoordinate coordinate = new AbsoluteCoordinate(x, y, configuration.world);
		int startingEnergy = configuration.startingEnergy;
		List<Rule> rules;
		if (currentRuleSet == null)
			rules = new ArrayList<Rule>();
		else 
			rules = currentRuleSet.getRules();
		RuleBasedPixel newPixel = new RuleBasedPixel(currentColor,
                coordinate,
                startingEnergy, rules);
        configuration.world.set(newPixel);
    }
}
