package evopaint.Abstractions;

import evopaint.Configuration;
import java.awt.Point;

public class TransformationCalculation {

    public static Point fromScreenToWorld(Configuration configuration, Point screen, double scale, Point translation) {
        //System.out.println("screen: " + screen);
        Point translated = new Point(screen);
        translated.x /= scale;
        translated.y /= scale;
        translated.translate((-1) * translation.x, (-1) * translation.y);
        /*
        while (translated.x < 0) {
            translated.x += configuration.dimension.width;
        }
        while (translated.x > configuration.dimension.width) {
            translated.x -= configuration.dimension.width;
        }
        while (translated.y < 0) {
            translated.y += configuration.dimension.height;
        }
        while (translated.y > configuration.dimension.height) {
            translated.y -= configuration.dimension.height;
        }
         */
        //System.out.println("translated: " + translated);
        return translated;
    }

    public static Point fromWorldToScreen(Configuration configuration, Point world, double scale, Point translation) {
        Point translated = new Point(world);
        translated.translate((-1) * translation.x, (-1) * translation.y);
        translated.x *= scale;
        translated.y *= scale;
        return translated;
    }
}
