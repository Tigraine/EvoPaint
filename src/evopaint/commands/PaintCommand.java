package evopaint.commands;

import evopaint.pixel.Pixel;
import evopaint.World;
import evopaint.util.mapping.AbsoluteCoordinate;
import evopaint.util.Color;
import evopaint.util.logging.Logger;

import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;

public class PaintCommand extends AbstractCommand {
    private World world;

    public double getScale() {
        return scale;
    }

    public void setScale(double scale) {
        this.scale = scale;
    }

    private double scale;
    private final AffineTransform affineTransform;
    private int radius;

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = new Point(location);
        this.location.x /= scale;
        this.location.y /= scale;

        try {
            this.location = (Point) affineTransform.inverseTransform(this.location , this.location);
        } catch(NoninvertibleTransformException e) {
            Logger.log.error("Non convertable transformation created. This should not be possible");
        }
    }

    private Point location;

    public PaintCommand(World world, double scale, AffineTransform affineTransform, int radius) {
        this.world = world;
        this.scale = scale;
        this.affineTransform = affineTransform;
        this.radius = radius;

        // TODO: make the new attributes a parameter
    }

    public void execute() {
        //Config.log.debug(this);
        Logger.log.information("Executing Paint command on x: %s y: %s", location.x, location.y);
        for (int i = 0 - this.radius / 2; i < radius / 2; i++) {
            for (int j = 0 - this.radius / 2; j < this.radius / 2; j++) {
                int x = location.x + j;
                int y = location.y + i;
                Pixel pixie = this.world.get(x, y);
                if (pixie == null) {
                    pixie = new Pixel(world.getConfiguration().startingEnergy,
                            new Color(0xFFFF0000), new AbsoluteCoordinate(x, y, world));
                    world.set(x, y, pixie);
                }
                pixie.getColor().setInteger(0xFFFF0000);
            }
        }
    }
}

