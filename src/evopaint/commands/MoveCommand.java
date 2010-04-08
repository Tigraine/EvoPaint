package evopaint.commands;

import evopaint.Configuration;
import java.awt.Point;

public class MoveCommand extends AbstractCommand {

    private Configuration configuration;
    private Point translation;
    private Point source;
    private Point destination;

    public Point getTranslation() {
        return translation;
    }

    public void setTranslation(Point translation) {
        this.translation = translation;
    }

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

    public MoveCommand(Configuration configuration) {
        this.configuration = configuration;
    }

    public void execute() {
        assert(translation != null);
        assert(source != null);
        assert(destination != null);

        Point delta = new Point(0, 0);
        delta.x = destination.x - source.x;
        delta.y = destination.y - source.y;

        translation.translate(delta.x, delta.y);

        while (translation.x < (-1) * configuration.dimension.width) {
            translation.x += configuration.dimension.width;
        }
        while (translation.x > configuration.dimension.width) {
            translation.x -= configuration.dimension.width;
        }
        while (translation.y < (-1) * configuration.dimension.height) {
            translation.y += configuration.dimension.height;
        }
        while (translation.y > configuration.dimension.height) {
            translation.y -= configuration.dimension.height;
        }

        source = destination;
        destination = null;
    }
}

