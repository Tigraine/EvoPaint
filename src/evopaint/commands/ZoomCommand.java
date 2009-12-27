package evopaint.commands;

import evopaint.attributes.PixelPerceptionAttribute;
import evopaint.entities.Observer;

import evopaint.gui.Showcase;
import java.awt.Dimension;

public class ZoomCommand extends AbstractCommand {
    private final Observer observer;
    private int zoomAmount;
    private Showcase showcase;

    public ZoomCommand(Showcase showcase, Observer observer, int zoomAmount) {
        this.observer = observer;
        this.zoomAmount = zoomAmount;
        this.showcase = showcase;
    }

    public void execute() {
        PixelPerceptionAttribute ppa = (PixelPerceptionAttribute) this.observer.getAttribute(PixelPerceptionAttribute.class);
        assert(ppa != null);
        ppa.setZoom(Math.max(0, ppa.getZoom() + zoomAmount));
        this.showcase.setPreferredSize(new Dimension(ppa.getPerception().getWidth(), ppa.getPerception().getHeight()));
    }
}

