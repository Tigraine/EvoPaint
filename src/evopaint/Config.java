/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package evopaint;

import evopaint.pixel.relations.ColorAssimilationRelation;
import evopaint.pixel.relations.ColorCopyRelation;
import evopaint.pixel.relations.ColorMoveRelation;
import evopaint.pixel.relations.ColorPullRelation;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.Map;

import java.awt.Dimension;

/**
 *
 * @author tam
 */
public class Config {

    public final Dimension defaultDimension = new Dimension(200,200);
    public final int initialPopulationX = 150;
    public final int initialPopulationY = 150;
    public final int stepsPerRendering = 1;
    public final int numRelationThreads = 1;
    public final int backgroundColor = 0 & 0x000000;

    // if true, this option will override each and every setting for how
    // many relations of what type are used and run exactly one of each avtive
    // relations per entity at all times.
    public static final boolean oneRelationPerEntity = false;

    //public static final double mutationRate = 0.0;

    public Config() {}

    public ArrayList<Class> pixelRelationTypes = new ArrayList<Class>() {{
        add(ColorCopyRelation.class);
        add(ColorAssimilationRelation.class);
        add(ColorMoveRelation.class);
        add(ColorPullRelation.class);
    }};

    public Map<Class,Integer> numPixelRelations = new IdentityHashMap<Class,Integer>() {{
        put(ColorCopyRelation.class, defaultDimension.width*defaultDimension.height);
        put(ColorAssimilationRelation.class, defaultDimension.width*defaultDimension.height);
        put(ColorMoveRelation.class, defaultDimension.width*defaultDimension.height);
        put(ColorPullRelation.class, defaultDimension.width*defaultDimension.height);
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
