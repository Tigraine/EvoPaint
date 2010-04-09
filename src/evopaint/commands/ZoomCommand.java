package evopaint.commands;


import evopaint.gui.util.WrappingScalableCanvas;

public class ZoomCommand extends AbstractCommand {
    private boolean zoomIn;
    private WrappingScalableCanvas canvas;

    protected ZoomCommand(WrappingScalableCanvas canvas, boolean zoomIn) {
        this.zoomIn = zoomIn;
        this.canvas = canvas;
    }

    public void execute() {
         if (this.zoomIn) {
            this.canvas.scaleUp();
        } else {
            this.canvas.scaleDown();
        }
    }
}

