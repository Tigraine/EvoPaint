package evopaint.commands;

import evopaint.Config;
import evopaint.Entity;
import evopaint.attributes.ColorAttribute;
import evopaint.attributes.PixelPerceptionAttribute;
import evopaint.attributes.SpacialAttribute;
import evopaint.entities.Observer;
import evopaint.entities.World;
import evopaint.interfaces.IAttribute;
import java.awt.Dimension;
import java.awt.Point;
import java.util.IdentityHashMap;

public class MoveCommand extends AbstractCommand {
    private Observer observer;
    private Point src;
    private Point dst;

    public MoveCommand(Observer observer, Point src, Point dst) {
        this.observer = observer;
        this.src = src;
        this.dst = dst;
    }

    public void execute() {
        //Config.log.debug(this);
        //Config.log.information("Executing Move command from %s/%s to %s/%s", src.x, src.y, dst.x, dst.y);
        PixelPerceptionAttribute ppa = (PixelPerceptionAttribute) this.observer.getAttribute(PixelPerceptionAttribute.class);
        assert(ppa != null);
        int dx = dst.x - src.x;
        int dy = dst.y - src.y;
        System.out.println("" + String.format("Executing Move command %s / %s", dx, dy));

        ppa.translateViewOffset(dx, dy);
    }
}

