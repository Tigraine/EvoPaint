/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package evopaint;


import evopaint.interfaces.ITool;
import java.awt.Dimension;
import java.awt.geom.AffineTransform;

/**
 *
 * @author tam
 */
public class Configuration {
    public static final int COLORMODE_COLOR = 0;
    public static final int COLORMODE_FAIRY_DUST = 1;
    public static final int COLORMODE_USE_EXISTING = 2;

    public final Dimension dimension = new Dimension(300,300);
    public final Dimension initialPopulation = new Dimension(300, 300);
    public final int stepsPerRendering = 1;
    //public final int numThreads = 1;
    public final int backgroundColor = 0;

    public final int startingEnergy = 100000;

    // if set to true a pixel will stop working down his rule set once the first
    // rule matches
    public final boolean oneActionPerPixel = true;

    //public static final double mutationRate = 0.0;
    private boolean running = true;
    private Brush brush;
    private ITool activeTool;
    private AffineTransform affineTransform;
    private int zoom;

    public ITool getActiveTool() {
        return activeTool;
    }

    public void setActiveTool(ITool activeTool) {
        this.activeTool = activeTool;
    }

    public Brush getBrush() {
        return brush;
    }

    public void setBrush(Brush brush) {
        this.brush = brush;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public AffineTransform getAffineTransform() {
        return affineTransform;
    }

    public void setAffineTransform(AffineTransform affineTransform) {
        this.affineTransform = affineTransform;
    }

    public int getZoom() {
        return zoom;
    }

    public void setZoom(int zoom) {
        this.zoom = zoom;
    }

    public double getScale() {
        return zoom / 10;
    }

    public Configuration() {}

    /*
    TODO: Talk through with TAM
    public static Log getLogger(Object object) {
        //Only a sample of what can be done with this.
        if (object instanceof Entity)
            return new NullLog();
        return log;
    } */
}
