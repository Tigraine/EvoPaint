package evopaint.commands;

import evopaint.Config;
import evopaint.interfaces.AbstractCommand;

public class ZoomCommand extends AbstractCommand {
    private boolean in;

    public ZoomCommand(boolean in) {
        this.in = in;
    }

    public void execute() {
        if (in) {
            Config.zoom++;
        } else {
            Config.zoom = Math.max(0, Config.zoom - 1);
        }
    }
}

