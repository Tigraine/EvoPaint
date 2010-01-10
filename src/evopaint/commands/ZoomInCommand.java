package evopaint.commands;

import evopaint.gui.Showcase;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 10.01.2010
 * Time: 13:39:02
 * To change this template use File | Settings | File Templates.
 */
public class ZoomInCommand extends ZoomCommand {
    public ZoomInCommand(Showcase showcase)
    {
        super(showcase, true);
    }
}
