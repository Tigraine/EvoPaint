package evopaint.commands;

import java.awt.Point;

import evopaint.Configuration;
import evopaint.pixel.rulebased.RuleBasedPixel;
import evopaint.pixel.rulebased.RuleSet;

public class PickCommand extends AbstractCommand {

	private Point location;
	private final Configuration config;

	public PickCommand(Configuration config) {
		this.config = config;
	}
	
	@Override
	public void execute() {
		RuleBasedPixel pixel = config.world.get(location.x, location.y);
		if (pixel == null) return; 
		config.paint.changeCurrentColor(pixel.getPixelColor());
		
		if (pixel instanceof RuleBasedPixel) {
			RuleSet ruleSet = pixel.getRuleSet();
			if (ruleSet != null) {
				config.paint.changeCurrentRuleSet(ruleSet);
			}
		}
	}

	public void setLocation(Point location) {
		this.location = location;
		
	}

}
