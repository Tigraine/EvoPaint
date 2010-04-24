package evopaint.commands;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;

import evopaint.Configuration;
import evopaint.gui.MainFrame;
import evopaint.pixel.Pixel;
import evopaint.pixel.PixelColor;
import evopaint.pixel.rulebased.Rule;
import evopaint.pixel.rulebased.RuleBasedPixel;
import evopaint.util.mapping.AbsoluteCoordinate;

public class ImportCommand extends AbstractCommand {

	private final Configuration configuration;

	public ImportCommand(Configuration configuration) {
		this.configuration = configuration;
	}

	@Override
	public void execute() {
		JFileChooser jFileChooser = new JFileChooser();
		int result = jFileChooser.showOpenDialog(jFileChooser);
		if (result == JFileChooser.APPROVE_OPTION){
			File file = jFileChooser.getSelectedFile();
			BufferedImage img = null;
			try {
				img = ImageIO.read(file);
				int width = configuration.world.getWidth();
				if (img.getWidth() < width) 
					width = img.getWidth();
				int height = configuration.world.getHeight();
				if (img.getHeight() < height)
					height = img.getHeight();
				
				for(int x = 0; x < width; x++) {					
					for(int y = 0; y < height; y++) {
						int rgb = img.getRGB(x, y);
						createPixel(x, y, rgb);
					}
				}
			} catch (IOException ex) {
				
			}
		}
	}

	private void createPixel(int x, int y, int rgb) {
		RuleBasedPixel pixel = configuration.world.get(x, y);
		if (pixel == null){
			AbsoluteCoordinate coordinate = new AbsoluteCoordinate(x, y, configuration.world);
			PixelColor pixelColor = new PixelColor(rgb);
			configuration.world.set(new RuleBasedPixel(pixelColor, coordinate, configuration.startingEnergy, new ArrayList<Rule>()));
		}
		else {
			pixel.getPixelColor().setInteger(rgb);
		}
	}

}
