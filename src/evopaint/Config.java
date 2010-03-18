/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package evopaint;


import java.awt.Dimension;

/**
 *
 * @author tam
 */
public class Config {

    public final Dimension dimension = new Dimension(200,200);
    public final Dimension initialPopulation = new Dimension(200, 200);
    public final int stepsPerRendering = 1;
    //public final int numThreads = 1;
    public final int backgroundColor = 0;

    public final int startingEnergy = 100;

    // if set to true a pixel will stop working down his rule set once the first
    // rule matches
    public final boolean oneActionPerPixel = true;

    //public static final double mutationRate = 0.0;

    public Config() {}

    /*
    TODO: Talk through with TAM
    public static Log getLogger(Object object) {
        //Only a sample of what can be done with this.
        if (object instanceof Entity)
            return new NullLog();
        return log;
    } */
}
