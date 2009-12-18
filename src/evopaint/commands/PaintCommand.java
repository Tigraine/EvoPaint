package evopaint.commands;

import evopaint.Entity;
import evopaint.attributes.ColorAttribute;
import evopaint.attributes.SpacialAttribute;
import evopaint.entities.World;
import evopaint.interfaces.AbstractCommand;
import evopaint.interfaces.IAttribute;
import java.awt.Point;
import java.util.IdentityHashMap;

public class PaintCommand extends AbstractCommand {
    private World world;
    private Point location;
    private int radius;
    private IdentityHashMap<Class,IAttribute> newAttributes;

    public PaintCommand(World world, Point location, int radius) {
        this.world = world;
        this.location = location;
        this.radius = radius;

        // TODO: make the new attributes a parameter
        this.newAttributes = new IdentityHashMap<Class,IAttribute>();
        this.newAttributes.put(ColorAttribute.class, new ColorAttribute(0xFFF0000));
        this.newAttributes.put(SpacialAttribute.class, new SpacialAttribute(new Point(location)));
    }

    public void execute() {
        //Config.log.debug(this);
        for (int i = 0; i < this.radius; i++) {
            for (int j = 0; j < this.radius; j++) {
                Point target = new Point(this.location.x, this.location.y);
                Entity entity = this.world.locationToEntity(target);
                entity.setAttributes(this.newAttributes);
            }
        }
    }
}

