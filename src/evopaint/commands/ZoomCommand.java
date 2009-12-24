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
        PixelPerceptionAttribute ppa = (PixelPerceptionAttribute) this.observer.getAttribute(PixelPerceptionAttribute.class);
        assert(ppa != null);
        ppa.setZoom(Math.max(0, ppa.getZoom() + zoomAmount));
    }
}

