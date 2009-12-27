package evopaint.commands;

import evopaint.EvoPaint;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 18.12.2009
 * Time: 00:32:15
 * To change this template use File | Settings | File Templates.
 */
public class PauseCommand extends AbstractCommand {
    private EvoPaint evopaint;

    public PauseCommand(EvoPaint evopaint) {
        this.evopaint = evopaint;
    }

    public void execute() {
        evopaint.setRunning(false);
        //ResumeCommand resumeCommand = new ResumeCommand(null);
        //resumeCommand.execute();
    }
}

