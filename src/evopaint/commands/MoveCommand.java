package evopaint.commands;

import evopaint.Configuration;
import java.awt.Point;
import java.awt.geom.AffineTransform;

public class MoveCommand extends AbstractCommand {

    private double dxtrans, dytrans;
    private AffineTransform at;
    private Point source;
    private Point destination;
    private int imageWidth;
    private int imageHeight;

    public Point getSource() {
        return source;
    }

    public void setSource(Point source) {
        this.source = source;
    }

    public Point getDestination() {
        return destination;
    }

    public void setDestination(Point destionation) {
        this.destination = destionation;
    }

    public double getScale() {
        return scale;
    }

    public void setScale(double scale) {
        this.scale = scale;
    }

    private double scale;

    private void translate(Point src, Point dst, double scale)
    {
        this.dxtrans = (dst.x - src.x) / scale;
        this.dytrans = (dst.y - src.y) / scale;
    }

    public MoveCommand(Configuration configuration) {
        this.at = configuration.affineTransform;
        this.imageWidth = configuration.dimension.width;
        this.imageHeight = configuration.dimension.height;
    }

    public void execute() {
        assert(scale != 0);
        assert(source != null);
        assert(destination != null);
        translate(source, destination, scale);
        // translate transform
        at.translate(dxtrans, dytrans);

        // and clamp it a little
        double dx = at.getTranslateX();
        double dy = at.getTranslateY();

        while (dx < (-1) * imageWidth) {
            at.translate(imageWidth, 0);
            dx = at.getTranslateX();
        }
        while (dx > imageWidth) {
            at.translate((-1) * imageWidth, 0);
            dx = at.getTranslateX();
        }
        while (dy < (-1) * imageHeight) {
            at.translate(0, imageHeight);
            dy = at.getTranslateY();
        }
        while (dy > imageHeight) {
            at.translate(0, (-1) * imageHeight);
            dy = at.getTranslateY();
        }

        source = destination;
    }
}

