package evopaint.gui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import evopaint.gui.util.IOverlay;
import evopaint.gui.util.WrappingScalableCanvas;

public class SelectionIndicatorOverlay extends Rectangle implements IOverlay {
	private WrappingScalableCanvas canvas;
	
	@Override
    public void setBounds(Rectangle bounds) {
        super.setBounds(bounds);
        this.x = bounds.x;
        this.y = bounds.y;
        this.width = bounds.width;
        this.height = bounds.height;
    }

	
    public SelectionIndicatorOverlay(WrappingScalableCanvas canvas, Rectangle bounds) {
        super(bounds);
        this.canvas = canvas;
    }

    public void paint(Graphics2D g2) {

        // prepare soft-xor painting
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .5f));

        // this would make the overlay look transparent white
        // imageG2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .5f));
        // imageG2.setColor(Color.white);

        canvas.fill(this);
    }

}
