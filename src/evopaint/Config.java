/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package evopaint;

import evopaint.pixel.relations.ColorAssimilationRelation;
import evopaint.pixel.relations.ColorCopyRelation;
import evopaint.pixel.relations.ColorMoveRelation;
import evopaint.pixel.relations.PixelCopyRelation;
import evopaint.pixel.relations.PixelMoveRelation;
import evopaint.pixel.relations.SynapticRelation;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.Map;

import java.awt.Dimension;

/**
 *
 * @author tam
 */
public class Config {

    public final Dimension defaultDimension = new Dimension(150,150);
    public final int initialPopulationX = 100;
    public final int initialPopulationY = 100;
    public final int stepsPerRendering = 1;
    public final int numRelationThreads = 1;
    public final int backgroundColor = 0 & 0x000000;

    // if true, this option will override each and every setting for how
    // many relations of what type are used and run exactly one of each avtive
    // relations per pixel at all times.
    public boolean pixelsAct = false;

    // if true, each pixel has exactly one relation and will choose it by its success
    // since there is no "general relation", pixelsAct must be set to true for this to work
    public boolean pixelsChooseRelation = false;

    //public static final double mutationRate = 0.0;

    public Config() {}

    public ArrayList<Class> pixelRelationTypes = new ArrayList<Class>() {{
        add(ColorCopyRelation.class);
        add(ColorAssimilationRelation.class);
        add(ColorMoveRelation.class);
        add(SynapticRelation.class);
        add(PixelCopyRelation.class);
        add(PixelMoveRelation.class);
    }};

    public Map<Class,Integer> numPixelRelations = new IdentityHashMap<Class,Integer>() {{
        put(ColorCopyRelation.class, defaultDimension.width*defaultDimension.height);
        put(ColorAssimilationRelation.class, defaultDimension.width*defaultDimension.height);
        put(ColorMoveRelation.class, defaultDimension.width*defaultDimension.height);
        put(SynapticRelation.class, defaultDimension.width*defaultDimension.height);
        put(PixelCopyRelation.class, defaultDimension.width*defaultDimension.height);
        put(PixelMoveRelation.class, defaultDimension.width*defaultDimension.height);
    }};

    /*
    TODO: Talk through with TAM
    public static Log getLogger(Object object) {
        //Only a sample of what can be done with this.
        if (object instanceof Entity)
            return new NullLog();
        return log;
    } */
}
