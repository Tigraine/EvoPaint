package evopaint.commands;

import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class MoveCommand extends AbstractCommand {

    private double dxtrans, dytrans;
    private AffineTransform at;
    private BufferedImage img;
    private Point source;
    private Point destination;

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

    public MoveCommand(AffineTransform at, BufferedImage img) {
        this.at = at;
        this.img = img;
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
        int w = img.getWidth();
        int h = img.getHeight();

        while (dx < (-1) * w) {
            at.translate(w, 0);
            dx = at.getTranslateX();
        }
        while (dx > w) {
            at.translate((-1) * w, 0);
            dx = at.getTranslateX();
        }
        while (dy < (-1) * h) {
            at.translate(0, h);
            dy = at.getTranslateY();
        }
        while (dy > h) {
            at.translate(0, (-1) * h);
            dy = at.getTranslateY();
        }

        source = destination;
    }
}

