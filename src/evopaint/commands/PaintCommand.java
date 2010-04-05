package evopaint.commands;


import evopaint.Brush;
import evopaint.Configuration;
import evopaint.World;
import evopaint.Abstractions.ScaleCalculation;
import evopaint.util.logging.Logger;


import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;

public class PaintCommand extends AbstractCommand {
    private Brush brush;
    private double scale;
    private final AffineTransform affineTransform;
    private Point location;

    public double getScale() {
        return scale;
    }

    public void setScale(double scale) {
        this.scale = scale;
    }

    public Brush getBrush() {
        return brush;
    }
    
    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = ScaleCalculation.FromScreenToWorld(location, scale);

        try {
            this.location = (Point) affineTransform.inverseTransform(this.location , this.location);
        } catch(NoninvertibleTransformException e) {
            Logger.log.error("Non convertable transformation created. This should not be possible");
        }
    }

    public PaintCommand(Configuration configuration, double scale, AffineTransform affineTransform) {
        this.brush = configuration.brush;
        this.scale = scale;
        this.affineTransform = affineTransform;
    }

    public void execute() {
        //Config.log.debug(this);
        Logger.log.information("Executing Paint command on x: %s y: %s", location.x, location.y);
        brush.paint(location.x, location.y);
    }
}

