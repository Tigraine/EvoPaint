/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package evopaint;


import evopaint.interfaces.IRandomNumberGenerator;
import evopaint.interfaces.ITool;
import evopaint.pixel.Pixel;
import evopaint.pixel.PixelColor;
import evopaint.pixel.Rule;
import evopaint.pixel.RuleSet;
import evopaint.pixel.State;
import evopaint.pixel.actions.AssimilationAction;
import evopaint.pixel.actions.RewardAction;
import evopaint.pixel.conditions.ColorLikenessCondition;
import evopaint.pixel.conditions.EnergyCondition;
import evopaint.pixel.interfaces.IAction;
import evopaint.pixel.interfaces.ICondition;
import evopaint.pixel.interfaces.IRule;
import evopaint.pixel.misc.NumberComparisonOperator;
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
   
    //public final int numThreads = 1;
    public final int backgroundColor = 0;
    
    public final int startingEnergy = 2000000;

    // if set to true a pixel will stop working down his rule set once the first
    // rule matches
    public final boolean oneActionPerPixel = true;

    //public static final double mutationRate = 0.0;

    // these values are configured through the GUI or by the application
    public boolean running = true;
    public World world;
    public Brush brush;
    public ITool activeTool;
    public AffineTransform affineTransform;
    public int zoom;

    public IRandomNumberGenerator rng;

    public double getScale() {
        return zoom / 10;
    }

    public RuleSet createDefaultRuleSet() {

        List<IRule> rules = new ArrayList<IRule>();

        List<ICondition> conditions = new ArrayList<ICondition>();
        List<RelativeCoordinate> directions = new ArrayList<RelativeCoordinate>();
        directions.add(RelativeCoordinate.SELF);
        conditions.add(new EnergyCondition(directions, NumberComparisonOperator.GREATER_THAN, 80));
        directions = new ArrayList<RelativeCoordinate>();
        directions.add(RelativeCoordinate.WEST);
        directions.add(RelativeCoordinate.NORTH);
        directions.add(RelativeCoordinate.SOUTH);
        directions.add(RelativeCoordinate.EAST);
        IAction action = new AssimilationAction(20, directions, PixelColor.HSB);
        rules.add(new Rule(conditions, action));

        conditions = new ArrayList<ICondition>();
        directions = new ArrayList<RelativeCoordinate>();
        directions.add(RelativeCoordinate.SELF);
        conditions.add(new ColorLikenessCondition(directions, NumberComparisonOperator.LESS_THAN, 75, PixelColor.H, new PixelColor(0x00FF00, rng)));
        directions = new ArrayList<RelativeCoordinate>();
        directions.add(RelativeCoordinate.SELF);
        action = new RewardAction(0, directions, 80);
        rules.add(new Rule(conditions, action));

        conditions = new ArrayList<ICondition>();
        directions = new ArrayList<RelativeCoordinate>();
        directions.add(RelativeCoordinate.SELF);
        conditions.add(new ColorLikenessCondition(directions, NumberComparisonOperator.GREATER_OR_EQUAL, 90, PixelColor.H, new PixelColor(0x0000ff, rng)));
        directions = new ArrayList<RelativeCoordinate>();
        directions.add(RelativeCoordinate.SELF);
        action = new RewardAction(0, directions, 80);
        //rules.add(new Rule(conditions, action));

        List<State> possibleStates = new ArrayList();
        State initialState = new State("START");
        possibleStates.add(initialState);

        RuleSet ruleSet = new RuleSet("test ruleset", rules, possibleStates, initialState);

        return ruleSet;
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
