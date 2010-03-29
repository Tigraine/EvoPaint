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
import evopaint.pixel.State;
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
        IAction action = new AssimilationAction(20, directions, new ColorDimensions(true, true, true));
        rules.add(new Rule(conditions, action));

        conditions = new ArrayList<ICondition>();
        directions = new ArrayList<RelativeCoordinate>();
        directions.add(RelativeCoordinate.SELF);
        conditions.add(new ColorLikenessCondition(directions, NumberComparisonOperator.LESS_THAN, 75, new ColorDimensions(true, false, false), new PixelColor(0x00FF00, rng)));
        directions = new ArrayList<RelativeCoordinate>();
        directions.add(RelativeCoordinate.SELF);
        action = new RewardAction(0, directions, 80);
        rules.add(new Rule(conditions, action));

        conditions = new ArrayList<ICondition>();
        directions = new ArrayList<RelativeCoordinate>();
        directions.add(RelativeCoordinate.SELF);
        conditions.add(new ColorLikenessCondition(directions, NumberComparisonOperator.GREATER_OR_EQUAL, 90, new ColorDimensions(true, false, false), new PixelColor(0x0000ff, rng)));
        directions = new ArrayList<RelativeCoordinate>();
        directions.add(RelativeCoordinate.SELF);
        action = new RewardAction(0, directions, 80);
        //rules.add(new Rule(conditions, action));

        String loremIpsum = "Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat. Ut wisi enim ad minim veniam, quis nostrud exerci tation ullamcorper suscipit lobortis nisl ut aliquip ex ea commodo consequat. Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse molestie consequat, vel illum dolore eu feugiat nulla facilisis at vero eros et accumsan et iusto odio dignissim qui blandit praesent luptatum zzril delenit augue duis dolore te feugait nulla facilisi. Nam liber tempor cum soluta nobis eleifend option congue nihil imperdiet doming id quod mazim placerat facer possim assum. Typi non habent claritatem insitam; est usus legentis in iis qui facit eorum claritatem. Investigationes demonstraverunt lectores legere me lius quod ii legunt saepius. Claritas est etiam processus dynamicus, qui sequitur mutationem consuetudium lectorum. Mirum est notare quam littera gothica, quam nunc putamus parum claram, anteposuerit litterarum formas humanitatis per seacula quarta decima et quinta decima. Eodem modo typi, qui nunc nobis videntur parum clari, fiant sollemnes in futurum.";
        RuleSet ruleSet = new RuleSet("test ruleset", loremIpsum, rules);

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
