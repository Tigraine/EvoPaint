package evopaint.commands;

import evopaint.Configuration;
import evopaint.gui.util.WrappingScalableCanvas;
import java.awt.Point;

public class MoveCommand extends AbstractCommand {

    private Configuration configuration;
    private WrappingScalableCanvas canvas;
    private Point source;
    private Point destination;

    public WrappingScalableCanvas getCanvas() {
        return canvas;
    }

    public void setCanvas(WrappingScalableCanvas canvas) {
        this.canvas = canvas;
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
        assert(canvas != null);
        assert(source != null);
        assert(destination != null);

        canvas.translateInUserSpace(source, destination);

        source = destination;
        destination = null;
    }
}

