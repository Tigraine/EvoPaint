/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint;

import evopaint.pixel.Pixel;
import evopaint.gui.MainFrame;
import evopaint.util.mapping.ParallaxMap;
import java.awt.image.BufferedImage;

/**
 *
 * @author tam
 */
public class EvoPaint {
    private World world;
    private boolean running = true;
    MainFrame frame;
    private Config configuration;
    private Perception perception;

    public EvoPaint() {
        this.configuration = new Config();

        // create empty world
        long time =0;
        this.world = new World(
                new Pixel[configuration.dimension.width * configuration.dimension.height],
                time, configuration);
        
        // create Image
        perception = new Perception(
                configuration.dimension.width, configuration.dimension.height, BufferedImage.TYPE_INT_RGB);
        perception.percieve(this.world);

        this.frame = new MainFrame(this);
    }

    public void work() {
        int i = 0;
        int stepsPerRendering = this.world.getConfiguration().stepsPerRendering;
        while (true) {
     
            if (this.running == false) {
                try { Thread.sleep(500); } catch (InterruptedException e) {}
                continue;
            }

            this.world.step();
              
            if (i % stepsPerRendering == 0) {
               perception.percieve(this.world);
               frame.getShowcase().repaint();
               //this.frame.getShowcase().paintImmediately(0, 0, this.frame.getShowcase().getWidth(), this.frame.getShowcase().getHeight());
               i = 0;
            }
            
            i++;
        }
    }

    public BufferedImage getImage() {
        return perception.getImage();
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
