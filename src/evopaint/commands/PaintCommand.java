package evopaint.commands;


import evopaint.Configuration;
import evopaint.Paint;
import evopaint.util.logging.Logger;


import java.awt.Point;

public class PaintCommand extends AbstractCommand {
    private Configuration configuration;
    private Point location;

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
    }

    public PaintCommand(Configuration configuration) {
        this.configuration = configuration;
    }

    public void execute() {
        //Config.log.debug(this);
        Logger.log.information("Executing Paint command on x: %s y: %s", location.x, location.y);
        configuration.brush.paint(location.x, location.y);

        if (false == (configuration.paint.getColorMode() == Paint.NO_COLOR &&
                configuration.paint.getRuleSet() == null) &&
                false == configuration.paintHistory.contains(configuration.paint)) {
            configuration.paintHistory.addFirst(configuration.paint);
            if (configuration.paintHistory.size() > configuration.paintHistorySize) {
                configuration.paintHistory.removeLast();
            }
        }
    }
}

