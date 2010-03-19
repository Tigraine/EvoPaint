package evopaint.commands;

import evopaint.entities.Pixel;
import evopaint.entities.World;
import evopaint.gui.MainFrame;
import evopaint.pixel.attributes.ColorAttribute;
import evopaint.util.Logger;

import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;

public class PaintCommand extends AbstractCommand {
    private World world;
    private int color;
    private MainFrame mf;

    public double getScale() {
        return scale;
    }

    public void setScale(double scale) {
        this.scale = scale;
    }

    private double scale;
    private final AffineTransform affineTransform;
    private int radius;

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

    public PaintCommand(World world, MainFrame mf, double scale, AffineTransform affineTransform, int radius,int color) {
    	//public PaintCommand(World world, double scale, AffineTransform affineTransform, int radius, int color) {
    	this.mf=mf;
        this.world = world;
        this.scale = scale;
        this.affineTransform = affineTransform;
        this.radius = radius;
        this.color = color;

        // TODO: make the new attributes a parameter
    }

    public void execute() {
        //Config.log.debug(this);
        Logger.log.information("Executing Paint command on x: %s y: %s", location.x, location.y);
        for (int i = 0 - this.radius / 2; i < radius / 2; i++) {
            for (int j = 0 - this.radius / 2; j < this.radius / 2; j++) {
                Point point = new Point(this.location.x + i, this.location.y + j);


               // IdentityHashMap<Class,IAttribute> newAttributes = new IdentityHashMap<Class,IAttribute>();
                //newAttributes.put(ColorAttribute.class, new ColorAttribute(0xFFFF0000));
                //newAttributes.put(SpacialAttribute.class, new SpacialAttribute(point, new Dimension(1,1)));
                Pixel pixie = this.world.locationToPixel(point);
               
                if(!(mf.getPop().getcBRandom())){
                	pixie.setColorAttribute(new ColorAttribute(color));
                }else{
                	pixie.setColorAttribute(new ColorAttribute(world.getRandomNumberGenerator().nextPositiveInt()));
                }

            }
        }
    }

	public void setColor(int color) {
		this.color = color;
	}
}

