/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package evopaint.entities;

import evopaint.Config;
import evopaint.RandomNumberGeneratorWrapper;
import evopaint.PixelRelation;
import evopaint.Relator;
import evopaint.interfaces.IAttribute;
import evopaint.interfaces.IRandomNumberGenerator;
import evopaint.pixel.attributes.PartnerSelectionAttribute;
import evopaint.pixel.matchers.RGBMatcher;
import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;

import evopaint.util.Logger;
import org.uncommons.maths.random.CellularAutomatonRNG;
import org.uncommons.maths.random.DefaultSeedGenerator;
import org.uncommons.maths.random.SeedException;
import org.uncommons.maths.random.SeedGenerator;

/**
 *
 * @author tam
 */
public class World extends System {

    private Dimension dimension;
    private Config configuration;
    private long time;
    private IRandomNumberGenerator randomNumberGenerator;

    public Dimension getDimension() {
        return dimension;
    }

    public void init() {
        this.initRNG();
        this.createEntities();
        this.createRelations();
    }

    public Pixel locationToPixel(Point location) {
        Point loc = (this.clamp(location));
        return this.pixels.get(this.dimension.width * loc.y + loc.x);
    }

    /**
     * resets the entity in the location-entity translation table to be empty
     * removes the entity from the system
     *
     * @param pixel entity to be removed
     */
    @Override
    public void remove(Pixel pixel) {
        pixel.getAttributes().clear();
        pixel.setColor(configuration.backgroundColor);
    }

    public void step() {

        if (time == Long.MAX_VALUE) {
            java.lang.System.out.println("time overflowed");
        }
 
        Logger.log.information("Time: %s, Relations: %s", new Object[]{time, this.relations.size()});
       
        Collections.shuffle(this.relations, this.randomNumberGenerator.getRandom());

        if (this.configuration.numRelationThreads > 1) {
            this.parallel();
        } else {
            this.serial();
        }

        this.time++;
    }

    private Point clamp(Point p) {
        int sizeX = this.configuration.defaultDimension.width;
        int sizeY = this.configuration.defaultDimension.height;

        while (p.x < 0) {
            p.x += sizeX;
        }
        while (p.x >= sizeX) {
            p.x -= sizeX;
        }
        while (p.y < 0) {
            p.y += sizeY;
        }
        while (p.y >= sizeY) {
            p.y -= sizeY;
        }
        return p;
    }

    private void createEntities() {
        for (int i = 0; i < this.dimension.width * this.dimension.height; i++) {
            IdentityHashMap<Class, IAttribute> attributesForPixel =
                        new IdentityHashMap<Class, IAttribute>();
                Point origin = new Point(i % this.dimension.width, i / this.dimension.width);
                int color = this.configuration.backgroundColor;
                this.pixels.add(new Pixel(color, origin, attributesForPixel));
        }

        for (int y = 0; y < this.configuration.initialPopulationY; y++) {
            for (int x = 0; x < this.configuration.initialPopulationX; x++) {

                int color = this.randomNumberGenerator.nextPositiveInt();
                Point location = new Point(
                        this.configuration.defaultDimension.width / 2 - this.configuration.initialPopulationX / 2 + x,
                        this.configuration.defaultDimension.height / 2 - this.configuration.initialPopulationY / 2 + y);


                Pixel pixie = locationToPixel(location);
                pixie.setColor(color);
                        
                //pixie.getAttributes().put(PartnerSelectionAttribute.class,
                  //      new PartnerSelectionAttribute(new RGBMatcher(), 0.1f, 0.9f));
            }
        }
    }

    private void createRelations() {
        if (this.configuration.oneRelationPerEntity == true) {
            this.createOneRelationPerEntity();
        } else {
            this.createRandomRelations();
        }
    }

    private void createOneRelationPerEntity() {
        try {
            for (Class pixelRelationType : this.configuration.pixelRelationTypes) {
                for (Pixel p : this.pixels) {
                    PixelRelation r = (PixelRelation) pixelRelationType.newInstance();
                    r.setA(p);
                    r.resetB(this, this.randomNumberGenerator);
                    this.relations.add(r);
                }
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
            java.lang.System.exit(1);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            java.lang.System.exit(1);
        }
    }

    private void createRandomRelations() {
        try {
            for (Class pixelRelationType : this.configuration.pixelRelationTypes) {
                int numRels = this.configuration.numPixelRelations.get(pixelRelationType);
                for (int i = 0; i < numRels; i++) {
                    PixelRelation r = (PixelRelation) pixelRelationType.newInstance();
                    r.reset(this, this.randomNumberGenerator);
                    this.relations.add(r);
                }
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
            java.lang.System.exit(1);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            java.lang.System.exit(1);
        }
    }

    private void serial() {
        for (PixelRelation relation : this.relations) {
            if (!relation.relate(this.configuration, this.randomNumberGenerator)) {
                if (this.configuration.oneRelationPerEntity == true) {
                    relation.resetB(this, this.randomNumberGenerator);
                } else {
                    relation.reset(this, this.randomNumberGenerator);
                }
            }
        }
    }

    private void parallel() {
        List<List<PixelRelation>> partitionedRelations = this.partition(this.relations, this.configuration.numRelationThreads);

        // make threads
        List<Thread> relatorThreads = new ArrayList<Thread>();
        for (List<PixelRelation> part : partitionedRelations) {

            // get random seed out of our rng
            byte [] seed = new byte[4];
            this.randomNumberGenerator.getRandom().nextBytes(seed);

            // and seed a new rng for each thread, so they can do random shit in
            // a well defined order (without race conditions on the main rng)
            Thread relator = new Relator(this, part, new RandomNumberGeneratorWrapper(new CellularAutomatonRNG(seed)), configuration);
            relatorThreads.add(relator);
            relator.start();
        }

        // join threads
        for (Thread relatorThread : relatorThreads) {
            try {
                relatorThread.join();
            }
            catch (InterruptedException ex) {
                Logger.log.warning("Exception during Relating %s", ex.getMessage());
            }
        }
    }

    private List<List<PixelRelation>> partition(List<PixelRelation> relations, int numChunks) {

        List<List<PixelRelation>> ret = new ArrayList<List<PixelRelation>>();

        int chunkSize = relations.size() / numChunks;
        int rest = relations.size() % numChunks;
        for (int i = 0; i < numChunks; i++) {
            List subList = relations.subList(i * chunkSize, (i + 1) * chunkSize + (i < rest ? 1 : 0));
            ret.add(subList);
        }

        ///* do not ever try to do it this way. the ternary is just fine...
        // * adding the rest like this will cause concurrent modification exceptions
        // * in parallel operation mode
        // */
        // for (int i = 0; i < rest; i++) {
        //    ret.get(i).add(relations.get(relations.size() - i - 1));
        // }

        return ret;
    }

    public Config getConfiguration() {
        return configuration;
    }

    private void initRNG() {
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
        randomNumberGenerator = new RandomNumberGeneratorWrapper(new CellularAutomatonRNG(seed));
    }

    public IRandomNumberGenerator getRandomNumberGenerator() {
        return randomNumberGenerator;
    }

    public World(List<Pixel> pixels, List<PixelRelation> relations, Dimension dimension, long time, Config configuration) {
        super(pixels, relations);
        this.dimension = dimension;
        this.time = time;
        this.configuration = configuration;
        this.init();
    }
}
