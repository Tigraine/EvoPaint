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
import evopaint.attributes.PartnerSelectionAttribute;
import evopaint.attributes.RelationsAttribute;
import evopaint.attributes.PartsAttribute;
import evopaint.attributes.SpacialAttribute;
import evopaint.attributes.TemporalAttribute;
import evopaint.matchers.RGBMatcher;
import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

import evopaint.util.Logger;
import org.uncommons.maths.random.CellularAutomatonRNG;

/**
 *
 * @author tam
 */
public class World extends System {

    private Dimension cachedDimension;
    private Map<Point, Entity> locationsToEntities;
    private Config configuration;

    public void init() {
        this.clear();
        this.createEntities();
        this.createRelations();
    }

    public Entity locationToEntity(Point location) {
        return this.locationsToEntities.get(this.clamp(location));
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

        Entity target = this.locationToEntity(spacialAttribute.getOrigin());

        if (target != null) {
            ColorAttribute cat = (ColorAttribute) target.getAttribute(ColorAttribute.class);
            if (cat != null) {
                java.lang.System.err.println("ERROR: Occupied location chosen for adding (Space.java) " + spacialAttribute.getOrigin().x + "/" + spacialAttribute.getOrigin().y);
                java.lang.System.exit(1);
            }
        }

        this.locationsToEntities.put(spacialAttribute.getOrigin(), entity);
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

        Logger.log.information("Time: %s, Relations: %s", new Object[]{ta, ra.getRelations().size()});

        ta.increaseTime();
        List<Relation> relations = ra.getRelations();

        Collections.shuffle(relations, this.configuration.randomNumberGenerator.getRandom());

        if (this.configuration.numRelationThreads > 1) {
            this.parallel(relations);
        } else {
            this.serial(relations);
        }
    }

    private Point clamp(Point p) {
        int sizeX = this.cachedDimension.width;
        int sizeY = this.cachedDimension.height;

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
        for (int y = 0; y < this.cachedDimension.height; y++) {
            for (int x = 0; x < this.cachedDimension.width; x++) {
                Point origin = new Point(x, y);
                Dimension dimension = new Dimension(1, 1);
                SpacialAttribute spacialAttribute = new SpacialAttribute(origin, dimension);
                IdentityHashMap<Class,IAttribute> attributesForEntity = new IdentityHashMap<Class,IAttribute>();
                attributesForEntity.put(SpacialAttribute.class,spacialAttribute);
                Entity entity = new Entity(attributesForEntity);
                this.add(entity);
            }
        }
    }

    private void createEntities() {
        for (int y = 0; y < this.configuration.initialPopulationY; y++) {
            for (int x = 0; x < this.configuration.initialPopulationX; x++) {

                IdentityHashMap<Class, IAttribute> attributesForEntity =
                        new IdentityHashMap<Class, IAttribute>();

                int color = this.configuration.randomNumberGenerator.nextPositiveInt();
                attributesForEntity.put(ColorAttribute.class, new ColorAttribute(color));

                Point origin = new Point(
                        this.cachedDimension.width / 2 - this.configuration.initialPopulationX / 2 + x,
                        this.cachedDimension.height / 2 - this.configuration.initialPopulationY / 2 + y);
                Dimension dimension = new Dimension(1, 1);
                attributesForEntity.put(SpacialAttribute.class, new SpacialAttribute(origin, dimension));

                Pixel pixie = new Pixel(attributesForEntity);

                attributesForEntity.put(PartnerSelectionAttribute.class,
                        new PartnerSelectionAttribute(new RGBMatcher(), 0.1f, 0.9f));

                this.add(pixie);
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
        RelationsAttribute ra = (RelationsAttribute) this.attributes.get(RelationsAttribute.class);
        if (ra == null) {
            java.lang.System.out.println("We accidently the whole world");
            java.lang.System.exit(1);
        }

        PartsAttribute pa = (PartsAttribute) this.attributes.get(PartsAttribute.class);
        List<Entity> entities = pa.getParts();

        try {
            for (Class pixelRelationType : this.configuration.pixelRelationTypes) {
                for (Entity e : entities) {
                    Relation r = (Relation) pixelRelationType.newInstance();
                    r.setA(e);
                    r.resetB(this, this.configuration.randomNumberGenerator);
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

    private void createRandomRelations() {
        RelationsAttribute ra = (RelationsAttribute) this.attributes.get(RelationsAttribute.class);
        if (ra == null) {
            java.lang.System.out.println("We accidently the whole world");
            java.lang.System.exit(1);
        }

        try {
            for (Class pixelRelationType : this.configuration.pixelRelationTypes) {
                int numRels = this.configuration.numPixelRelations.get(pixelRelationType);
                for (int i = 0; i < numRels; i++) {
                    Relation r = (Relation) pixelRelationType.newInstance();
                    r.reset(this, this.configuration.randomNumberGenerator);
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

    private void serial(List<Relation> relations) {
        for (Relation relation : relations) {
            if (!relation.relate(this.configuration.randomNumberGenerator)) {
                if (this.configuration.oneRelationPerEntity == true) {
                    relation.resetB(this, this.configuration.randomNumberGenerator);
                } else {
                    relation.reset(this, this.configuration.randomNumberGenerator);
                }
            }
        }
    }

    private void parallel(List<Relation> relations) {
        List<List<Relation>> partitionedRelations = this.partition(relations, this.configuration.numRelationThreads);

        // make threads
        List<Thread> relatorThreads = new ArrayList<Thread>();
        for (List<Relation> part : partitionedRelations) {

            // get random seed out of our rng
            byte [] seed = new byte[4];
            this.configuration.randomNumberGenerator.getRandom().nextBytes(seed);

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

    private List<List<Relation>> partition(List<Relation> relations, int numChunks) {

        List<List<Relation>> ret = new ArrayList<List<Relation>>();

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

    public World(IdentityHashMap<Class, IAttribute> attributes, PartsAttribute pa,
            RelationsAttribute ra, SpacialAttribute sa, TemporalAttribute ta, Config configuration) {
        super(attributes, pa, ra);
        this.attributes.put(SpacialAttribute.class, sa);
        this.attributes.put(TemporalAttribute.class, ta);
        this.locationsToEntities = new HashMap<Point, Entity>();
        this.cachedDimension = sa.getDimension();
        this.configuration = configuration;
    }
}
