package evopaint.commands;

import evopaint.Config;
import evopaint.attributes.PixelPerceptionAttribute;
import evopaint.entities.Observer;
import evopaint.interfaces.AbstractCommand;

import java.awt.image.BufferedImage;

public class ZoomCommand extends AbstractCommand {
    private final Observer observer;
    private int zoomAmount;

    public ZoomCommand(Observer observer, int zoomAmount) {
        this.observer = observer;
        this.zoomAmount = zoomAmount;
    }

    public void execute() {
        int zoom = Math.max(0, Config.zoom + zoomAmount);
        Config.zoom = zoom;
        observer.setAttribute(PixelPerceptionAttribute.class,
                new PixelPerceptionAttribute(Config.sizeX, Config.sizeY, BufferedImage.TYPE_INT_RGB, zoom));
    }
}

