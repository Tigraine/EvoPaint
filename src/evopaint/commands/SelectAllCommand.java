package evopaint.commands;

import evopaint.Configuration;
import evopaint.Selection;
import evopaint.gui.Showcase;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 26.03.2010
 * Time: 15:30:58
 * To change this template use File | Settings | File Templates.
 */
public class SelectAllCommand extends AbstractCommand {
    private final Showcase showcase;
    private final Configuration config;

    public SelectAllCommand(Showcase showcase, Configuration config) {
        this.showcase = showcase;
        this.config = config;
    }

    public void execute() {
        Selection selection = new Selection(new Point(0, 0), new Point((int) config.dimension.getHeight(), (int) config.dimension.getWidth()));
        selection.setSelectionName("All");
        showcase.getCurrentSelections().add(selection);
    }
}
