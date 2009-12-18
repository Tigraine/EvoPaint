/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package evopaint;

import evopaint.util.Log;
import evopaint.interfaces.IRandomNumberGenerator;
import evopaint.relations.pixel.ColorAssimilationRelation;
import evopaint.relations.pixel.ColorCopyRelation;
import evopaint.util.ConsoleLog;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.Map;

import evopaint.util.LogLevel;
import org.uncommons.maths.random.CellularAutomatonRNG;
import org.uncommons.maths.random.DefaultSeedGenerator;
import org.uncommons.maths.random.SeedException;
import org.uncommons.maths.random.SeedGenerator;

/**
 *
 * @author tam
 */
public class Config {

    public static final int nrRenderings = 0;
    public static final int nrStepsPerRendering = 1;
    public static final int zoom = 3;

    public static final int sizeX = 150;
    public static final int sizeY = 150;
    public static final int initialPopulationX = 150;
    public static final int initialPopulationY = 150;
    
    public static final int numRelationThreads = 1;

    public static final int logLevel = LogLevel.ERROR;
    public static final int logVerbosity = Log.Verbosity.VERBOSEVERBOSE;
    public static Log log = new ConsoleLog(logLevel);

    // if true, this option will override each and every setting for how
    // many relations of what type are used and run exactly one of each avtive
    // relations per entity at all times.
    public static final boolean oneRelationPerEntity = true;

    //public static final double mutationRate = 0.0;

    public static ArrayList<Class> pixelRelationTypes = new ArrayList<Class>() {{
        //add(ColorCopyRelation.class);
        add(ColorAssimilationRelation.class);
    }};

    public static Map<Class,Integer> numPixelRelations = new IdentityHashMap<Class,Integer>() {{
        //put(ColorCopyRelation.class, Config.sizeX*Config.sizeY);
        put(ColorAssimilationRelation.class, Config.sizeX*Config.sizeY);
    }};

    // initialized by init()
    public static IRandomNumberGenerator randomNumberGenerator;

    public static void init() {
        Config.initRNG();
    }

    private static void initRNG() {
        // Random, SecureRandom, AESCounterRNG, CellularAutomatonRNG,
        // CMWC4096RNG, JavaRNG, MersenneTwisterRNG, XORShiftRNG

        // default seed size for cellularAutomatonRNG is 4 bytes;
        int seed_size_bytes = 4;

        // set fixed seed or null for generation
        byte [] seed = null;
       // byte [] seed = new byte [] { 1, 2, 3, 4 };

        // default seed generator checks some different approaches and will
        // always succeed
        if (seed == null) {
            SeedGenerator sg = DefaultSeedGenerator.getInstance();
            try {
                seed = sg.generateSeed(4);
            } catch (SeedException e) {
                log.error("got seed exception from default seed generator. this should not have happened.");
                java.lang.System.exit(1);
            }
        }
        Config.randomNumberGenerator = new RandomNumberGeneratorWrapper(new CellularAutomatonRNG(seed));
    }

    /*
    TODO: Talk through with TAM
    public static Log getLogger(Object object) {
        //Only a sample of what can be done with this.
        if (object instanceof Entity)
            return new NullLog();
        return log;
    } */
}
