/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint;

import evopaint.pixel.Pixel;
import evopaint.gui.MainFrame;
import evopaint.pixel.rulebased.interfaces.IAction;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author tam
 */
public class EvoPaint {

    private Configuration configuration;
    private World world;
    private Perception perception;
    private boolean running = true;
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

    public void setPerception(Perception perception) {
        this.perception = perception;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public void work() {
        while (true) {

            if (configuration.running == false) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                }
                continue;
            }

            this.perception.createImage(world);

            this.world.step();

            frame.getShowcase().repaint();

            //this.frame.getShowcase().paintImmediately(0, 0, this.frame.getShowcase().getWidth(), this.frame.getShowcase().getHeight());
        }
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public static void main(String args[]) {
        EvoPaint evopaint = new EvoPaint();

        evopaint.work();
    }
    
    public EvoPaint() {
        this.configuration = new Configuration();

        // create empty world
        long time = 0;
        this.world = new World(
                new Pixel[configuration.dimension.width * configuration.dimension.height],
                time, configuration);

        this.configuration.world = world;

        this.perception = new Perception(new BufferedImage(configuration.dimension.width, configuration.dimension.height,
                BufferedImage.TYPE_INT_RGB));

        this.perception.createImage(world);

        this.frame = new MainFrame(configuration, this);
    }
}
