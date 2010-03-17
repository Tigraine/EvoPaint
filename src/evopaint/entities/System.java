/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package evopaint.entities;

import evopaint.PixelRelation;
import evopaint.util.ParallaxMap;
import java.util.List;

/**
 *
 * @author tam
 */
public class System {
    protected ParallaxMap<Pixel> pixels;
    protected List<PixelRelation> relations;

    public ParallaxMap<Pixel> getPixels() {
        return pixels;
    }

    public List<PixelRelation> getRelations() {
        return relations;
    }

    public System(ParallaxMap<Pixel> pixels, List<PixelRelation> relations) {
        this.pixels = pixels;
        this.relations = relations;
    }

    public System() {}
}
