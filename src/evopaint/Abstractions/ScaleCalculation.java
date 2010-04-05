package evopaint.Abstractions;

import java.awt.Point;

public class ScaleCalculation {
	
	public static Point FromScreenToWorld(Point screen, double scale) {
		Point translated = new Point(screen);
		translated.x /= scale;
        translated.y /= scale;
        return translated;
	}
}
