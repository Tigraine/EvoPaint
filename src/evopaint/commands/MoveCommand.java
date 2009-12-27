package evopaint.commands;

import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class MoveCommand extends AbstractCommand {

    private double dxtrans, dytrans;
    private AffineTransform at;
    private BufferedImage img;

    public MoveCommand(Point src, Point dst, AffineTransform at, BufferedImage img) {
        this.at = at;
        this.dxtrans = (dst.x - src.x) / at.getScaleX();
        this.dytrans = (dst.y - src.y) / at.getScaleY();
        this.img = img;
    }

    public void execute() {
        
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
    }
}

