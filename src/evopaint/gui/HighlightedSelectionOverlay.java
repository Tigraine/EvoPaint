package evopaint.gui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import com.sun.java.swing.plaf.windows.WindowsInternalFrameTitlePane.ScalableIconUIResource;

import evopaint.Selection;
import evopaint.gui.util.IOverlay;
import evopaint.gui.util.WrappingScalableCanvas;

public class HighlightedSelectionOverlay implements IOverlay {

	private Selection selection;
	private WrappingScalableCanvas canvas;
	private Rectangle rect;
	public HighlightedSelectionOverlay(Selection selection, WrappingScalableCanvas canvas) {
		this.selection = selection;
		this.canvas = canvas;
		
		rect = new Rectangle(selection.getStartPoint(), new Dimension(selection.getEndPoint().x - selection.getStartPoint().x, selection.getEndPoint().y - selection.getStartPoint().y));
	}
	
	@Override
	public void paint(Graphics2D g2) {
		g2.setComposite(AlphaComposite
				.getInstance(AlphaComposite.SRC_OVER, .5f));
		
		canvas.draw(rect);
	}

}
