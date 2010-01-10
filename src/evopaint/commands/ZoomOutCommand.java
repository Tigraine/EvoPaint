package evopaint.commands;

import evopaint.gui.Showcase;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 10.01.2010
 * Time: 13:39:56
 * To change this template use File | Settings | File Templates.
 */
public class ZoomOutCommand extends ZoomCommand {
    public ZoomOutCommand(Showcase showcase) {
        super(showcase, false);
    }
}
