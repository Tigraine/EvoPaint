package evopaint.commands;


import evopaint.gui.MainFrame;
import evopaint.pixel.Pixel;
import evopaint.World;
import evopaint.gui.PaintOptionsPanel;
import evopaint.util.mapping.AbsoluteCoordinate;
import evopaint.pixel.PixelColor;
import evopaint.util.logging.Logger;


import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;

public class PaintCommand extends AbstractCommand {
    private World world;
    private MainFrame mf;
    private PaintOptionsPanel paintOptionsPanel;
    private double scale;
    private final AffineTransform affineTransform;

    public double getScale() {
        return scale;
    }

    public void setScale(double scale) {
        this.scale = scale;
    }
    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = new Point(location);
        this.location.x /= scale;
        this.location.y /= scale;

        try {
            this.location = (Point) affineTransform.inverseTransform(this.location , this.location);
        } catch(NoninvertibleTransformException e) {
            Logger.log.error("Non convertable transformation created. This should not be possible");
        }
    }

    private Point location;

    public PaintCommand(World world, MainFrame mf, double scale, AffineTransform affineTransform, PaintOptionsPanel paintOptionsPanel) {
    	//public PaintCommand(World world, double scale, AffineTransform affineTransform, int radius, int color) {
    	this.mf=mf;
        this.world = world;
        this.paintOptionsPanel = paintOptionsPanel;
        this.scale = scale;
        this.affineTransform = affineTransform;

        // TODO: make the new attributes a parameter
    }

    public void execute() {
        //Config.log.debug(this);
        Logger.log.information("Executing Paint command on x: %s y: %s", location.x, location.y);
        int brushSize = paintOptionsPanel.getBrushsize();
        for (int i = 0; i < brushSize; i++) {
            for (int j = 0; j < brushSize; j++) {
                int x = location.x + j;
                int y = location.y + i;
                Pixel pixie = this.world.get(x, y);
                int pixelColorInteger = 0;
                switch (mf.getPop().getColorMode()) {
                    case PaintOptionsPanel.COLORMODE_FAIRY_DUST:
                        pixelColorInteger = world.getRandomNumberGenerator().nextPositiveInt();
                        break;
                    case PaintOptionsPanel.COLORMODE_USE_EXISTING:
                        if (pixie == null) {
                            continue;
                        }
                        pixelColorInteger = pixie.getPixelColor().getInteger();
                        break;
                    case PaintOptionsPanel.COLORMODE_COLOR:
                        pixelColorInteger = paintOptionsPanel.getColor().getRGB();
                        break;
                    default:
                        Logger.log.error("colorMode not set", new Object());
                        System.exit(1);
                }
                if (pixie == null) {
                    pixie = new Pixel(world.getConfiguration().startingEnergy,
                        new PixelColor(pixelColorInteger), new AbsoluteCoordinate(x, y, world));
                    world.set(pixie);
                } else {
                    pixie.getPixelColor().setInteger(pixelColorInteger);
                }
                
            }
        }
    }
}

