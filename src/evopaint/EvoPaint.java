/*
 * EvoPaint.java
 */

package evopaint;

import evopaint.entities.World;
import evopaint.interfaces.IAttribute;
import evopaint.attributes.PartsAttribute;
import evopaint.attributes.PixelPerceptionAttribute;
import evopaint.attributes.RelationsAttribute;
import evopaint.entities.Observer;
import evopaint.relations.PixelPerceptionRelation;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * The main class of the application.
 */
public class EvoPaint extends JPanel {

    private World world;
    private Observer observer;
    private Relation perception;

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    private boolean running = true;

    @Override
    public void paint(Graphics g) {
        //g.clearRect(0, 0, Config.sizeX*Config.zoom, Config.sizeY*Config.zoom);
        g.drawImage(observer.getPerception(), 0, 0, null);
    }

    public EvoPaint() {
        Config.init();

        // create empty world
        List parts = new ArrayList<Entity>();
        PartsAttribute pa = new PartsAttribute(parts);

        List relations = new ArrayList<Relation>();
        RelationsAttribute ra = new RelationsAttribute(relations);
        long time = 0;
        this.world = new World(new IdentityHashMap<Class, IAttribute>(), pa, ra, time);
        this.world.init();

        // create observer
        PixelPerceptionAttribute ppa = new PixelPerceptionAttribute(Config.sizeX,
                Config.sizeY, BufferedImage.TYPE_INT_ARGB, Config.zoom);
        this.observer = new Observer(new IdentityHashMap<Class, IAttribute>(), ppa);

        // observe the world
        this.perception = new PixelPerceptionRelation(observer, world);

        this.setBackground(Color.WHITE);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        EvoPaint app = new EvoPaint();
        app.setPreferredSize(new Dimension(Config.sizeX*Config.zoom, Config.sizeY*Config.zoom));
        frame.add(app);
        frame.pack();
        frame.setVisible(true);
        app.work();
        System.exit(0);
    }

    public void work() {
        
        if (Config.nrRenderings == 0) {
            while (running) {
                this.world.step();
                boolean ret = this.perception.relate(Config.randomNumberGenerator);
                assert (ret);
                repaint();
            }
        } else {
            for (int i = 0; i < Config.nrRenderings && running; i++) {
                this.world.step();
                boolean ret = this.perception.relate(Config.randomNumberGenerator);
                assert (ret);
                repaint();
            }
        }

    }


}