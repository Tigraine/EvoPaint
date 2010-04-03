/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package evopaint;


import evopaint.interfaces.IRandomNumberGenerator;
import evopaint.interfaces.ITool;
import evopaint.pixel.ColorDimensions;
import evopaint.pixel.Pixel;
import evopaint.pixel.PixelColor;
import evopaint.pixel.rulebased.Rule;
import evopaint.pixel.rulebased.RuleSet;
import evopaint.pixel.rulebased.actions.AssimilationAction;
import evopaint.pixel.rulebased.actions.NoAction;
import evopaint.pixel.rulebased.actions.RewardAction;
import evopaint.pixel.rulebased.conditions.ColorLikenessCondition;
import evopaint.pixel.rulebased.conditions.EnergyCondition;
import evopaint.pixel.rulebased.interfaces.IAction;
import evopaint.pixel.rulebased.interfaces.ICondition;
import evopaint.pixel.rulebased.interfaces.IRule;
import evopaint.pixel.rulebased.util.NumberComparisonOperator;
import evopaint.pixel.rulebased.conditions.EmptyCondition;
import evopaint.pixel.rulebased.conditions.NoCondition;
import evopaint.util.FileHandler;
import evopaint.util.RandomNumberGeneratorWrapper;
import evopaint.util.logging.Logger;
import evopaint.util.mapping.RelativeCoordinate;
import java.awt.Dimension;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;
import org.uncommons.maths.random.CellularAutomatonRNG;
import org.uncommons.maths.random.DefaultSeedGenerator;
import org.uncommons.maths.random.SeedException;
import org.uncommons.maths.random.SeedGenerator;

/**
 *
 * @author tam
 */
public class Configuration {
    public static final int COLORMODE_COLOR = 0;
    public static final int COLORMODE_FAIRY_DUST = 1;
    public static final int COLORMODE_USE_EXISTING = 2;

    public Dimension dimension = new Dimension(300, 300);
    public final Dimension initialPopulation = new Dimension(300, 300);
    public final int stepsPerRendering = 1;

    public final int pixelType = Pixel.RULESET;

    // TODO make to list of objects of ICondition
    public static final List<ICondition> availableConditions = new ArrayList<ICondition>() {{
        add(new NoCondition());
        add(new EmptyCondition());
        add(new EnergyCondition());
        add(new ColorLikenessCondition());
    }};

    public static final List<IAction> availableActions = new ArrayList<IAction>() {{
        add(new NoAction());
        add(new AssimilationAction());
        add(new RewardAction());
    }};
   
    //public final int numThreads = 1;
    public final int backgroundColor = 0;
    
    public final int startingEnergy = 100;

    // if set to true a pixel will stop working down his rule set once the first
    // rule matches
    public final boolean oneActionPerPixel = true;

    //public static final double mutationRate = 0.0;

    // these values are configured through the GUI or by the application
    public boolean running = true;
    public FileHandler fileHandler;
    public World world;
    public Brush brush;
    public ITool activeTool;
    public AffineTransform affineTransform;
    public int zoom;

    public IRandomNumberGenerator rng;

    public double getScale() {
        return zoom / 10;
    }


    private IRandomNumberGenerator createRNG() {
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
                Logger.log.error("got seed exception from default seed generator. this should not have happened.");
                java.lang.System.exit(1);
            }
        }
        return new RandomNumberGeneratorWrapper(new CellularAutomatonRNG(seed));
    }

    public Configuration() {
        rng = createRNG();
        brush = new Brush(this);
        fileHandler = new FileHandler();
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
