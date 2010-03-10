/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint;

import evopaint.attributes.PartsAttribute;
import evopaint.attributes.PixelPerceptionAttribute;
import evopaint.attributes.RelationsAttribute;
import evopaint.attributes.SpacialAttribute;
import evopaint.attributes.TemporalAttribute;
import evopaint.entities.Observer;
import evopaint.entities.World;
import evopaint.gui.MainFrame;
import evopaint.interfaces.IAttribute;
import evopaint.relations.PixelPerceptionRelation;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;

/**
 *
 * @author tam
 */
public class EvoPaint {
    private PixelPerceptionRelation perception;
    private World world;
    private Observer observer;
    private boolean running = true;
    MainFrame frame;
    private Config configuration;

    public EvoPaint() {
        this.configuration = new Config();

        // create empty world
        List<Entity> parts = new ArrayList<Entity>();
        PartsAttribute pa = new PartsAttribute(parts);

        List<Relation> relations = new ArrayList<Relation>();
        RelationsAttribute ra = new RelationsAttribute(relations);
        SpacialAttribute sa = new SpacialAttribute(new Point(0, 0), new Dimension(configuration.defaultDimension));
        TemporalAttribute ta = new TemporalAttribute(0);
        this.world = new World(new IdentityHashMap<Class, IAttribute>(), pa,
                ra, sa, ta, configuration);
        this.world.init();

        // create observer
        PixelPerceptionAttribute ppa = new PixelPerceptionAttribute(
                sa.getDimension().width, sa.getDimension().height, BufferedImage.TYPE_INT_ARGB);
        this.observer = new Observer(new IdentityHashMap<Class, IAttribute>(),
                ppa);

        // observe the world
        this.perception = new PixelPerceptionRelation(observer, world);
        boolean ret = this.perception.relate(configuration.randomNumberGenerator);
        assert (ret);

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
               boolean ret = this.perception.relate(configuration.randomNumberGenerator);
               assert (ret);
               //this.frame.getShowcase().repaint(); // causes flickering cause painting is delayed until the image is being reconstructed
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
