package evopaint.commands;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import evopaint.Configuration;
import evopaint.Selection;
import evopaint.gui.util.IOverlay;
import evopaint.pixel.Pixel;

public class CopySelectionCommand extends AbstractCommand {

	private final Configuration config;
	private SelectionCopyOverlay overlay;
	private Point location;
	
	public CopySelectionCommand(Configuration config) {
		this.config = config;
	}
	
	public void setLocation(Point p) {
		location = p;
	}
	
	@Override
	public void execute() {
		Selection activeSelection = config.mainFrame.getShowcase().getActiveSelection();
		Rectangle rect = activeSelection.getRectangle();
		overlay = new SelectionCopyOverlay(rect.width, rect.height);
		for(int x = rect.x; x < rect.x + rect.width; x++) {
			for (int y = rect.y; y < rect.y + rect.width; y++) {
				Pixel pixel = config.world.get(x, y);
				if (pixel == null) continue;
					overlay.setRGB(x, y, pixel.getPixelColor().getInteger());
			}
		}
		
		config.mainFrame.getShowcase().subscribe(overlay);
	}
	
		
	private class SelectionCopyOverlay extends BufferedImage implements IOverlay {

		public SelectionCopyOverlay(int width, int height) {
			super(width, height, BufferedImage.TYPE_INT_RGB);
		}

		@Override
		public void paint(Graphics2D g2) {
			g2.drawImage(this, location.x, location.y, config.mainFrame.getShowcase());
		}
	}
}
