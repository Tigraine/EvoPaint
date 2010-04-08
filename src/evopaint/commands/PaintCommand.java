package evopaint.commands;


import evopaint.Configuration;
import evopaint.Abstractions.ScaleCalculation;
import evopaint.util.logging.Logger;


import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;

public class PaintCommand extends AbstractCommand {
    private Configuration configuration;
    private double scale;
    private final AffineTransform affineTransform;
    private Point location;

    public double getScale() {
        return scale;
    }

    public void setScale(double scale) {
        this.scale = scale;
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
        this.configuration = configuration;
        this.scale = scale;
        this.affineTransform = affineTransform;
    }

    public void execute() {
        //Config.log.debug(this);
        Logger.log.information("Executing Paint command on x: %s y: %s", location.x, location.y);
        configuration.brush.paint(location.x, location.y);

        if (false == configuration.paintHistory.contains(configuration.paint)) {
            configuration.paintHistory.addFirst(configuration.paint);
            if (configuration.paintHistory.size() > configuration.paintHistorySize) {
                configuration.paintHistory.removeLast();
            }
        }
    }
}

