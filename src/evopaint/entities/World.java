/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package evopaint.entities;

import evopaint.Config;
import evopaint.Entity;
import evopaint.RandomNumberGeneratorWrapper;
import evopaint.Relation;
import evopaint.Relator;
import evopaint.interfaces.IAttribute;
import evopaint.attributes.ColorAttribute;
import evopaint.attributes.RelationsAttribute;
import evopaint.attributes.PartsAttribute;
import evopaint.attributes.SpacialAttribute;
import evopaint.attributes.TemporalAttribute;
import evopaint.util.Log;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import org.uncommons.maths.random.CellularAutomatonRNG;

/**
 *
 * @author tam
 */
public class World extends System {

    private static Map<Point, Entity> locationsToEntities;

    public void init() {
        this.clear();
        this.createEntities();
        this.createRelations();
    }

    public static Entity locationToEntity(Point location) {
        return World.locationsToEntities.get(World.clamp(location));
    }

    @Override
    public void add(Entity entity) {
        super.add(entity);

        // if an entity consists of parts, we might want to add them
        PartsAttribute partsAttribute = (PartsAttribute) entity.getAttribute(PartsAttribute.class);
        if (partsAttribute != null) {
            // add its parts recursively
            Collection<Entity> parts = partsAttribute.getParts();
            for (Entity part : parts) {
                this.add(part);
            }
        }

        // can only add entities which have spacial means
        SpacialAttribute spacialAttribute = (SpacialAttribute) entity.getAttribute(SpacialAttribute.class);
        if (spacialAttribute == null) {
            return;
        }

        Entity target = World.locationToEntity(spacialAttribute.getLocation());

        if (target != null) {
            ColorAttribute cat = (ColorAttribute) target.getAttribute(ColorAttribute.class);
            if (cat != null) {
                java.lang.System.err.println("ERROR: Occupied location chosen for adding (Space.java) " + spacialAttribute.getLocation().x + "/" + spacialAttribute.getLocation().y);
                java.lang.System.exit(1);
            }
        }

        World.locationsToEntities.put(spacialAttribute.getLocation(), entity);
        return;
    }

    /**
     * resets the entity in the location-entity translation table to be empty
     * removes the entity from the system
     *
     * @param entity entity to be removed
     */
    @Override
    public void remove(Entity entity) {
        SpacialAttribute sa = (SpacialAttribute) entity.getAttribute(SpacialAttribute.class);
        assert(sa != null);
        entity.getAttributes().clear();
        entity.setAttribute(SpacialAttribute.class, sa);
        //super.remove(entity);
    }

    public void step() {

        // get time and relations
        TemporalAttribute ta = (TemporalAttribute) this.attributes.get(TemporalAttribute.class);
        RelationsAttribute ra = (RelationsAttribute) this.attributes.get(RelationsAttribute.class);

        if (ta == null || ra == null) {
            java.lang.System.out.println("We accidently the whole world");
            java.lang.System.exit(1);
        }

        if (Config.logLevel >= Log.Level.INFORMATION) {
            Config.log.information("");
            Config.log.information("Time: " + ta + ", Relations: " + ra.getRelations().size());
        }

        ta.increaseTime();
        List<Relation> relations = ra.getRelations();

        Collections.shuffle(relations, Config.randomNumberGenerator.getRandom());

        if (Config.numRelationThreads > 1) {
            this.parallel(relations);
        } else {
            this.serial(relations);
        }

    }

    private static Point clamp(Point p) {
        final int sizeX = Config.sizeX;
        final int sizeY = Config.sizeY;

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


    /**
     * fills the world with colorless, corporeal entities (think: space-slots)
     */
    private void clear() {
        for (int y = 0; y < Config.sizeY; y++) {
            for (int x = 0; x < Config.sizeX; x++) {
                Point location = new Point(x, y);
                SpacialAttribute spacialAttribute = new SpacialAttribute(location);
                IdentityHashMap<Class,IAttribute> attributesForEntity = new IdentityHashMap<Class,IAttribute>();
                attributesForEntity.put(SpacialAttribute.class,spacialAttribute);
                Entity entity = new Entity(attributesForEntity);
                this.add(entity);
            }
        }
    }

    private void createEntities() {
        for (int y = 0; y < Config.initialPopulationY; y++) {
            for (int x = 0; x < Config.initialPopulationX; x++) {

                IdentityHashMap<Class, IAttribute> attributesForEntity =
                        new IdentityHashMap<Class, IAttribute>();

                int color = Config.randomNumberGenerator.nextPositiveInt();
                ColorAttribute colorAttribute = new ColorAttribute(color);

                Point location = new Point(
                        Config.sizeX / 2 - Config.initialPopulationX / 2 + x,
                        Config.sizeY / 2 - Config.initialPopulationY / 2 + y);
                SpacialAttribute spacialAttribute = new SpacialAttribute(location);

                Pixel pixie = new Pixel(attributesForEntity, colorAttribute, spacialAttribute);

                this.add(pixie);
            }
        }
    }

    private void createRelations() {
        RelationsAttribute ra = (RelationsAttribute) this.attributes.get(RelationsAttribute.class);
        if (ra == null) {
            java.lang.System.out.println("We accidently the whole world");
            java.lang.System.exit(1);
        }

        try {
            for (Class pixelRelationType : Config.pixelRelationTypes) {
                int numRels = Config.numPixelRelations.get(pixelRelationType);
                for (int i = 0; i < numRels; i++) {
                    Relation r = (Relation) pixelRelationType.newInstance();
                    this.resetRelation(r);
                    ra.getRelations().add(r);
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

    private void resetRelation(Relation relation) {
        // pick random A
        relation.setA(World.locationToEntity((Config.randomNumberGenerator.nextLocation())));

        // choose B from a's immediate environment
        SpacialAttribute sa = (SpacialAttribute) relation.getA().getAttribute(SpacialAttribute.class);
        assert (sa != null);
        Point newLocation = new Point(sa.getLocation());
        newLocation.translate(Config.randomNumberGenerator.nextPositiveInt(3) - 1,
                Config.randomNumberGenerator.nextPositiveInt(3) - 1);
        relation.setB(World.locationToEntity(newLocation));
    }

    private void serial(List<Relation> relations) {
        for (Relation relation :relations) {
            if (!relation.relate()) {
                this.resetRelation(relation);
            }
        }
    }

    private void parallel(List<Relation> relations) {
        List<List<Relation>> partitionedRelations = this.partition(relations, Config.numRelationThreads);

        // make threads
        List<Thread> relatorThreads = new ArrayList<Thread>();
        for (List<Relation> part : partitionedRelations) {

            // get random seed out of our rng
            byte [] seed = new byte[4];
            Config.randomNumberGenerator.getRandom().nextBytes(seed);

            // and seed a new rng for each thread, so they can do random shit in
            // a well defined order (without race conditions on the main rng)
            Thread relator = new Relator(part, new RandomNumberGeneratorWrapper(new CellularAutomatonRNG(seed)));
            relatorThreads.add(relator);
            relator.start();
        }

        // join threads
        for (Thread relatorThread : relatorThreads) {
            try {
                relatorThread.join();
            }
            catch (InterruptedException ex) {
                Config.log.warning("Exception during Relating" + ex.getMessage());
            }
        }
    }

    private List<List<Relation>> partition(List<Relation> relations, int numChunks) {

        List<List<Relation>> ret = new ArrayList<List<Relation>>();

        int chunkSize = relations.size() / numChunks;
        for (int i = 0; i < numChunks; i++) {
            List subList = relations.subList(i * chunkSize, (i + 1) * chunkSize);
            ret.add(subList);
        }

        int rest = relations.size() % numChunks;
        for (int i = 0; i < rest; i++) {
            ret.get(i).add(relations.get(relations.size() - i - 1));
        }

        return ret;
    }

    public World(IdentityHashMap<Class, IAttribute> attributes, PartsAttribute pa, RelationsAttribute ra, long time) {
        super(attributes, pa, ra);
        this.attributes.put(TemporalAttribute.class, new TemporalAttribute(time));
        World.locationsToEntities = new HashMap<Point, Entity>();
    }
}
