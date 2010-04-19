/*
 *  Copyright (C) 2010 Markus Echterhoff <tam@edu.uni-klu.ac.at>
 *
 *  This file is part of EvoPaint.
 *
 *  EvoPaint is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with EvoPaint.  If not, see <http://www.gnu.org/licenses/>.
 */

package evopaint;


import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import evopaint.interfaces.IRandomNumberGenerator;
import evopaint.interfaces.ITool;
import evopaint.pixel.Pixel;
import evopaint.pixel.rulebased.Action;
import evopaint.pixel.rulebased.Condition;
import evopaint.pixel.rulebased.actions.AssimilationAction;
import evopaint.pixel.rulebased.actions.CopyAction;
import evopaint.pixel.rulebased.actions.ChangeEnergyAction;
import evopaint.pixel.rulebased.actions.MoveAction;
import evopaint.pixel.rulebased.actions.SetColorAction;
import evopaint.pixel.rulebased.actions.PartnerProcreationAction;
import evopaint.pixel.rulebased.conditions.ColorLikenessConditionColor;
import evopaint.pixel.rulebased.conditions.ColorLikenessConditionMyColor;
import evopaint.pixel.rulebased.conditions.EnergyCondition;
import evopaint.pixel.rulebased.conditions.ExistenceCondition;
import evopaint.pixel.rulebased.conditions.TrueCondition;
import evopaint.pixel.rulebased.targeting.Qualifier;
import evopaint.pixel.rulebased.targeting.qualifiers.ColorLikenessQualifierColor;
import evopaint.pixel.rulebased.targeting.qualifiers.ColorLikenessQualifierMyColor;
import evopaint.pixel.rulebased.targeting.qualifiers.ExistenceQualifier;
import evopaint.pixel.rulebased.targeting.qualifiers.EnergyQualifier;
import evopaint.util.FileHandler;
import evopaint.util.RandomNumberGeneratorWrapper;
import evopaint.util.logging.Logger;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import org.uncommons.maths.random.CellularAutomatonRNG;
import org.uncommons.maths.random.DefaultSeedGenerator;
import org.uncommons.maths.random.SeedException;
import org.uncommons.maths.random.SeedGenerator;

/**
 *
 * @author Markus Echterhoff <tam@edu.uni-klu.ac.at>
 */
public class Configuration {
    public static final int OPERATIONMODE_AGENT_SIMULATION = 0;
    public static final int OPERATIONMODE_CELLULAR_AUTOMATON = 1;

    public static final int RUNLEVEL_RUNNING = 2;
    public static final int RUNLEVEL_PAINTING_ONLY = 1;
    public static final int RUNLEVEL_STOP = 0;

    public final int pixelType = Pixel.RULESET;

    public static final List<Condition> AVAILABLE_CONDITIONS = new ArrayList<Condition>() {{
        add(new TrueCondition());
        add(new ExistenceCondition());
        add(new EnergyCondition());
        add(new ColorLikenessConditionColor());
        add(new ColorLikenessConditionMyColor());
    }};

    public static final List<Action> AVAILABLE_ACTIONS = new ArrayList<Action>() {{
        add(new ChangeEnergyAction());
        add(new CopyAction());
        add(new MoveAction());
        add(new AssimilationAction());
        add(new PartnerProcreationAction());
        add(new SetColorAction());
    }};

    public static final List<Qualifier> AVAILABLE_QUALIFIERS = new ArrayList<Qualifier>() {{
        add(new ExistenceQualifier());
        add(new EnergyQualifier());
        add(new ColorLikenessQualifierColor());
        add(new ColorLikenessQualifierMyColor());
    }};

    public IRandomNumberGenerator rng;
    public XStream xStream;
    public FileHandler fileHandler;
    public World world; // TODO make use of me
    public Brush brush;
    public Paint paint;
    public ITool activeTool; // TODO make use of me

    // BEGIN user configurable
    public int operationMode = OPERATIONMODE_AGENT_SIMULATION;
    public int fps = 30;
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
        paint = new Paint(this);
        xStream = new XStream(new DomDriver());
        fileHandler = new FileHandler(xStream);
    }
}
