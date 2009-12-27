package evopaint.commands;


import evopaint.gui.Showcase;

public class ZoomCommand extends AbstractCommand {
    private boolean zoomIn;
    private Showcase showcase;

    public ZoomCommand(Showcase showcase, boolean zoomIn) {
        this.zoomIn = zoomIn;
        this.showcase = showcase;
    }

    public void execute() {
         if (this.zoomIn) {
            this.showcase.incrementScale();
        } else {
            this.showcase.decrementScale();
        }
    }
}

