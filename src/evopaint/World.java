/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package evopaint;

import evopaint.pixel.Pixel;
import evopaint.pixel.PixelColor;
import evopaint.pixel.RuleBasedPixel;
import evopaint.pixel.RuleSet;

import evopaint.util.logging.Logger;
import evopaint.util.mapping.AbsoluteCoordinate;
import evopaint.util.mapping.ParallaxMap;

/**
 *
 * @author tam
 */
public class World extends ParallaxMap<Pixel> {

    private Configuration configuration;
    private long time;

    public void init() {
        this.createPixels();
    }

    public void step() {

        if (time == Long.MAX_VALUE) {
            java.lang.System.out.println("time overflowed");
        }
 
        Logger.log.information("Time: %s", new Object[]{time});
       
        //Collections.shuffle(this.relations, this.rng.getRandom());

        //if (this.configuration.numThreads > 1) {
            //this.parallel();
        //} else {
            this.serial();
        //}

        this.time++;

        //if (time == 1000)
          //  java.lang.System.exit(0);

    }

    private void createPixels() {
        RuleSet ruleSet = configuration.createDefaultRuleSet();

        System.out.println("Test with the following rules for every pixel:");
        System.out.println(ruleSet.toString());
        
        for (int y = 0; y < configuration.initialPopulation.height; y++) {
            for (int x = 0; x < configuration.initialPopulation.width; x++) {

                AbsoluteCoordinate location = new AbsoluteCoordinate(configuration.dimension.width / 2 - configuration.initialPopulation.width / 2 + x,
                        configuration.dimension.height / 2 - configuration.initialPopulation.height / 2 + y, this);

                PixelColor pixelColor = new PixelColor(configuration.rng.nextPositiveInt(), configuration.rng);

                Pixel newPixel = null;
                switch (configuration.pixelType) {
                    case Pixel.RULESET:
                        newPixel = new RuleBasedPixel(pixelColor, location, configuration.startingEnergy, ruleSet);
                                break;
                    default: assert(false);
                }
                set(newPixel);
            }
        }
    }

    public void set(Pixel pixel) {
        super.set(pixel.getLocation().x, pixel.getLocation().y, pixel);
    }

    private void serial() {
        int [] indices = getShuffledIndices(configuration.rng);
        
        for (int i = 0; i < indices.length; i++) {
            Pixel pixie = get(indices[i]);
            if (pixie.isAlive()) {
                pixie.act(this);
            } else {
                set(indices[i], null);
            }
        }
    }
/*
    private void parallel() {
        List<List<PixelRelation>> partitionedRelations = this.partition(this.relations, this.configuration.numRelationThreads);

        // make threads
        List<Thread> relatorThreads = new ArrayList<Thread>();
        for (List<PixelRelation> part : partitionedRelations) {

            // get random seed out of our rng
            byte [] seed = new byte[4];
            this.rng.getRandom().nextBytes(seed);

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

        // do not ever try to do it this way. the ternary is just fine...
        // adding the rest like this will cause concurrent modification exceptions
        // in parallel operation mode
        // 
        // for (int i = 0; i < rest; i++) {
        //    ret.get(i).add(relations.get(relations.size() - i - 1));
        // }

        return ret;
    }
*/
    public Configuration getConfiguration() {
        return configuration;
    }

    public World(Pixel [] pixels, long time, Configuration configuration) {
        super(pixels, configuration.dimension.width, configuration.dimension.height);
        this.time = time;
        this.configuration = configuration;
    }
}
