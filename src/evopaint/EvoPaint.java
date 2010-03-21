/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint;

import evopaint.pixel.Pixel;
import evopaint.gui.MainFrame;
import java.awt.image.BufferedImage;

/**
 *
 * @author tam
 */
public class EvoPaint {
    private World world;
    private boolean running = true;
    MainFrame frame;
    private Configuration configuration;
    private Perception perception;
    
    public EvoPaint() {
        this.configuration = new Configuration();

        // create empty world
        long time =0;
        this.world = new World(
                new Pixel[configuration.dimension.width * configuration.dimension.height],
                time, configuration);

        this.perception = new Perception(new BufferedImage
                (configuration.dimension.width, configuration.dimension.height,
                BufferedImage.TYPE_INT_RGB));

        this.perception.createImage(world);

        this.frame = new MainFrame(this);
    }

    public void work() {
        while (true) {
     
            if (this.running == false) {
                try { Thread.sleep(500); } catch (InterruptedException e) {}
                continue;
            }

            this.perception.createImage(world);

            this.world.step();

            frame.getShowcase().repaint();

           //this.frame.getShowcase().paintImmediately(0, 0, this.frame.getShowcase().getWidth(), this.frame.getShowcase().getHeight());
        }
    }

    public Perception getPerception() {
        return perception;
    }

    public World getWorld() {
        return world;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public boolean isRunning() {
        return running;
    }

    public static void main(String args[]) {
        EvoPaint evopaint = new EvoPaint();
        evopaint.work();
    }
}
