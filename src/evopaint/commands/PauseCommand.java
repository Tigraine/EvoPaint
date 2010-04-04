package evopaint.commands;

import evopaint.Configuration;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 18.12.2009
 * Time: 00:32:15
 * To change this template use File | Settings | File Templates.
 */
public class PauseCommand extends AbstractCommand {
    private Configuration configuration;

    public PauseCommand(Configuration configuration) {
        this.configuration = configuration;
    }

    public void execute() {
        configuration.runLevel = Configuration.RUNLEVEL_PAINTING_ONLY;
    }
}

