package evopaint.commands;

import evopaint.Config;
import evopaint.interfaces.AbstractCommand;

public class ZoomCommand extends AbstractCommand {
    private int zoomAmount;

    public ZoomCommand(int zoomAmount) {
        this.zoomAmount = zoomAmount;
    }

    public void execute() {
        Config.zoom = Math.max(0, Config.zoom + zoomAmount);
    }
}

