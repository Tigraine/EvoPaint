package evopaint.commands;

import evopaint.EvoPaint;
import evopaint.interfaces.ICommand;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 18.12.2009
 * Time: 00:44:51
 * To change this template use File | Settings | File Templates.
 */
public class ResumeCommand implements ICommand {
    private EvoPaint evoPaint;

    public ResumeCommand(EvoPaint evoPaint) {
        this.evoPaint = evoPaint;
    }

    public void execute() {
        evoPaint.setRunning(true);
    }
}
