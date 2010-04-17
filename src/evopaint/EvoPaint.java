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

package evopaint;

import evopaint.pixel.Pixel;
import evopaint.gui.MainFrame;
import evopaint.util.ExceptionHandler;

import java.awt.image.BufferedImage;

/**
 *
 * @author Markus Echterhoff <tam@edu.uni-klu.ac.at>
 */
public class EvoPaint {

    private Configuration configuration;
    private Perception perception;
    MainFrame frame;

    public MainFrame getFrame() {
        return frame;
    }

    public void setFrame(MainFrame frame) {
        this.frame = frame;
    }

    public Perception getPerception() {
        return perception;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public void work() {
        this.perception.createImage(configuration.world);
        frame.getShowcase().repaint();
        long timeStamp = System.currentTimeMillis();

        while (true) {

            if (configuration.runLevel == Configuration.RUNLEVEL_RUNNING) {
                configuration.world.step();
            }
            else if (configuration.runLevel == Configuration.RUNLEVEL_PAINTING_ONLY) {
                long timedifference = System.currentTimeMillis() - timeStamp;
                if (timedifference < 1000 / configuration.fps) {
                    try {
                    Thread.sleep(timedifference);
                    } catch (InterruptedException ex) {
                    }
                }
            } else {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                }
                continue;
            }

            long currentTime = System.currentTimeMillis();
            if (currentTime - timeStamp > 1000 / configuration.fps) {
                timeStamp = currentTime;
                this.perception.createImage(configuration.world);
                frame.getShowcase().repaint();
            }
        }
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public static void main(String args[]) {
        try {
            EvoPaint evopaint = new EvoPaint();
            
            evopaint.work();
        } catch (Exception ex) {
            ExceptionHandler.handle(ex);
        }
    }
    
    public EvoPaint() {
        this.configuration = new Configuration();

        // create empty world
        long time = 0;
        this.configuration.world = new World(
                new Pixel[configuration.dimension.width * configuration.dimension.height],
                time, configuration);

        this.perception = new Perception(new BufferedImage(configuration.dimension.width, configuration.dimension.height,
                BufferedImage.TYPE_INT_RGB));

        this.perception.createImage(configuration.world);

        this.frame = new MainFrame(configuration, this);
    }
}
