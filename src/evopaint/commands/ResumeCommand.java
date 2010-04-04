package evopaint.commands;

import evopaint.Configuration;
import evopaint.interfaces.ICommand;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 18.12.2009
 * Time: 00:44:51
 * To change this template use File | Settings | File Templates.
 */
public class ResumeCommand implements ICommand {
    private Configuration configuration;

    public ResumeCommand(Configuration configuration) {
        this.configuration = configuration;
    }

    public void execute() {
        configuration.runLevel = Configuration.RUNLEVEL_RUNNING;
    }
}
