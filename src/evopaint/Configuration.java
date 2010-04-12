/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package evopaint;


import evopaint.interfaces.IRandomNumberGenerator;
import evopaint.interfaces.ITool;
import evopaint.pixel.Pixel;
import evopaint.pixel.PixelColor;
import evopaint.pixel.rulebased.Action;
import evopaint.pixel.rulebased.Condition;
import evopaint.pixel.rulebased.actions.AssimilationAction;
import evopaint.pixel.rulebased.actions.CopyAction;
import evopaint.pixel.rulebased.actions.IdleAction;
import evopaint.pixel.rulebased.actions.MoveAction;
import evopaint.pixel.rulebased.conditions.ColorLikenessCondition;
import evopaint.pixel.rulebased.conditions.EnergyCondition;
import evopaint.pixel.rulebased.conditions.ExistenceCondition;
import evopaint.pixel.rulebased.conditions.TrueCondition;
import evopaint.pixel.rulebased.targeting.IActionTarget;
import evopaint.pixel.rulebased.targeting.IConditionTarget;
import evopaint.pixel.rulebased.targeting.QualifiedActionTarget;
import evopaint.pixel.rulebased.targeting.QualifiedConditionTarget;
import evopaint.pixel.rulebased.targeting.Qualifier;
import evopaint.pixel.rulebased.targeting.QuantifiedConditionTarget;
import evopaint.pixel.rulebased.targeting.SpecifiedActionTarget;
import evopaint.pixel.rulebased.targeting.SpecifiedConditionTarget;
import evopaint.pixel.rulebased.targeting.qualifiers.ExistenceQualifier;
import evopaint.pixel.rulebased.targeting.qualifiers.LeastEnergyQualifier;
import evopaint.pixel.rulebased.targeting.qualifiers.MostEnergyQualifier;
import evopaint.pixel.rulebased.targeting.qualifiers.NonExistenceQualifier;
import evopaint.util.FileHandler;
import evopaint.util.RandomNumberGeneratorWrapper;
import evopaint.util.logging.Logger;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.LinkedList;
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

    public static final int RUNLEVEL_RUNNING = 2;
    public static final int RUNLEVEL_PAINTING_ONLY = 1;
    public static final int RUNLEVEL_STOP = 0;

    public final int pixelType = Pixel.RULESET;

    public static final List<Condition> AVAILABLE_CONDITIONS = new ArrayList<Condition>() {{
        add(new TrueCondition());
        add(new ExistenceCondition());
        add(new EnergyCondition());
        add(new ColorLikenessCondition());
    }};

    public static final List<Action> AVAILABLE_ACTIONS = new ArrayList<Action>() {{
        add(new IdleAction());
        add(new AssimilationAction());
        add(new CopyAction());
        add(new MoveAction());
    }};

    public static final List<Qualifier> AVAILABLE_QUALIFIERS = new ArrayList<Qualifier>() {{
        add(new ExistenceQualifier());
        add(new NonExistenceQualifier());
        add(new LeastEnergyQualifier());
        add(new MostEnergyQualifier());
    }};
    
    public static final List<IConditionTarget> AVAILABLE_CONDITION_TARGETS = new ArrayList<IConditionTarget>() {{
        add(new SpecifiedConditionTarget());
        add(new QualifiedConditionTarget());
        add(new QuantifiedConditionTarget());
    }};

    public static final List<IActionTarget> AVAILABLE_ACTION_TARGETS = new ArrayList<IActionTarget>() {{
        add(new SpecifiedActionTarget());
        add(new QualifiedActionTarget());
    }};

    public IRandomNumberGenerator rng;
    public FileHandler fileHandler;
    public World world; // TODO make use of me
    public Brush brush;
    public Paint paint;
    public ITool activeTool; // TODO make use of me
    public LinkedList<Paint> paintHistory;

    // BEGIN user configurable
    public int fps = 60;
    public Dimension dimension = new Dimension(300, 300);
    public int backgroundColor = 0;
    public int startingEnergy = 100;
    public int runLevel = Configuration.RUNLEVEL_RUNNING;
    public int paintHistorySize = 7;
    // END user configurable

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
        paint = new Paint(this, Paint.COLOR, Paint.NO_RULE_SET, new PixelColor(0xFF0000), null);
        fileHandler = new FileHandler();
        paintHistory = new LinkedList<Paint>();
    }
}
