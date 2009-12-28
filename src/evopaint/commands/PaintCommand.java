package evopaint.commands;

import evopaint.Config;
import evopaint.Entity;
import evopaint.attributes.ColorAttribute;
import evopaint.attributes.SpacialAttribute;
import evopaint.entities.World;
import evopaint.interfaces.IAttribute;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.util.IdentityHashMap;

public class PaintCommand extends AbstractCommand {
    private World world;
    private int radius;
    private Point location;

    public PaintCommand(World world, Point location, double scale, AffineTransform at, int radius) {
        this.world = world;
        this.radius = radius;

        this.location = new Point(location);
        this.location.x /= scale;
        this.location.y /= scale;
        try {
            this.location = (Point)at.inverseTransform(this.location , this.location);
        } catch(NoninvertibleTransformException e) {
            Config.log.error("Non convertable transformation created. This should not be possible");
        }
        
        // TODO: make the new attributes a parameter
    }

    public void execute() {
        //Config.log.debug(this);
        Config.log.information("Executing Paint command on x: %s y: %s", location.x, location.y);
        for (int i = 0 - this.radius / 2; i < radius / 2; i++) {
            for (int j = 0 - this.radius / 2; j < this.radius / 2; j++) {
                Point point = new Point(this.location.x + i, this.location.y + j);


                IdentityHashMap<Class,IAttribute> newAttributes = new IdentityHashMap<Class,IAttribute>();
                newAttributes.put(ColorAttribute.class, new ColorAttribute(0xFFFF0000));
                newAttributes.put(SpacialAttribute.class, new SpacialAttribute(point, new Dimension(1,1)));
                Entity entity = this.world.locationToEntity(point);
                entity.setAttributes(newAttributes);
            }
        }
    }
}

