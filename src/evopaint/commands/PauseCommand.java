package evopaint.commands;

import evopaint.EvoPaint;
import evopaint.entities.World;
import evopaint.interfaces.AbstractCommand;
import evopaint.interfaces.ICommand;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 18.12.2009
 * Time: 00:32:15
 * To change this template use File | Settings | File Templates.
 */
public class PauseCommand extends AbstractCommand {
    private EvoPaint evoPaint;

    public PauseCommand(EvoPaint evoPaint) {
        this.evoPaint = evoPaint;
    }

    public void execute() {
        //evoPaint.setRunning(false);
    }
}

