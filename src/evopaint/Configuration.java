/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package evopaint;


import evopaint.interfaces.ITool;
import java.awt.Dimension;

/**
 *
 * @author tam
 */
public class Configuration {

    public final Dimension dimension = new Dimension(200,200);
    public final Dimension initialPopulation = new Dimension(200, 200);
    public final int stepsPerRendering = 1;
    //public final int numThreads = 1;
    public final int backgroundColor = 0;

    public final int startingEnergy = 1000000;

    // if set to true a pixel will stop working down his rule set once the first
    // rule matches
    public final boolean oneActionPerPixel = true;

    //public static final double mutationRate = 0.0;
    private boolean running = true;
    private Brush brush;
    private ITool activeTool;

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
