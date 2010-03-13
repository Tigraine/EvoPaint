/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint;

import evopaint.entities.Observer;
import evopaint.entities.Pixel;
import evopaint.entities.World;
import evopaint.gui.MainFrame;
import evopaint.interfaces.IAttribute;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;

/**
 *
 * @author tam
 */
public class EvoPaint {
    private World world;
    private Observer observer;
    private boolean running = true;
    MainFrame frame;
    private Config configuration;

    public EvoPaint() {
        this.configuration = new Config();

        // create empty world
        List<Pixel> pixels = new ArrayList<Pixel>(configuration.defaultDimension.width * configuration.defaultDimension.height);
        List<PixelRelation> relations = new ArrayList<PixelRelation>();
        Dimension dimension = new Dimension(configuration.defaultDimension);
        long time =0;
        this.world = new World(pixels, relations, dimension, time, configuration);
        
        // create observer
        Perception perception = new Perception(
                dimension.width, dimension.height, BufferedImage.TYPE_INT_RGB);
        this.observer = new Observer(perception);
        this.observer.percieve(this.world);

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
               this.observer.percieve(this.world);
               this.frame.getShowcase().paintImmediately(0, 0, this.frame.getShowcase().getWidth(), this.frame.getShowcase().getHeight());
               i = 0;
            }
            
            i++;
        }
    }

    public BufferedImage getImage() {
        return this.observer.getPerception();
    }

    public Observer getObserver() {
        return observer;
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
