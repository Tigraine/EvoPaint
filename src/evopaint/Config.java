/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package evopaint;

import evopaint.util.Log;
import evopaint.interfaces.IRandomNumberGenerator;
import evopaint.relations.pixel.CopyColorRelation;
import evopaint.util.ConsoleLog;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.Map;
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
    public static final int sizeX = 100;
    public static final int sizeY = 100;
    public static final int initialPopulationX = 100;
    public static final int initialPopulationY = 100;
    public static final int zoom = 4;
    public static final int logLevel = Log.Level.WARNING;
    public static final int logVerbosity = Log.Verbosity.VERBOSEVERBOSE;
    public static final int logFormat = Log.Format.COMPACT;
    public static final int numRelationThreads = 1;
    public static Log log = new ConsoleLog();
    //public static final double mutationRate = 0.0;

    public static ArrayList<Class> pixelRelationTypes = new ArrayList<Class>() {{
        add(CopyColorRelation.class);
    }};

    public static Map<Class,Integer> numPixelRelations = new IdentityHashMap<Class,Integer>() {{
        put(CopyColorRelation.class, Config.sizeX*Config.sizeY);
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
                java.lang.System.err.println("got seed exception from default seed generator. this should not have happened.");
                java.lang.System.exit(1);
            }
        }
        Config.randomNumberGenerator = new RandomNumberGeneratorWrapper(new CellularAutomatonRNG(seed));
    }
}
