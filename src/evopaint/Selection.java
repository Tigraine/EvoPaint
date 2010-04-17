package evopaint;

import java.awt.*;
import java.util.Observable;

import evopaint.gui.HighlightedSelectionOverlay;
import evopaint.gui.util.IOverlay;
import evopaint.gui.util.WrappingScalableCanvas;

/**
 * Created by IntelliJ IDEA. User: daniel Date: 07.03.2010 Time: 12:41:14 To
 * change this template use File | Settings | File Templates.
 */
public class Selection extends Observable implements IOverlay {
	private Point startPoint;
	private Point endPoint;
	private String selectionName;
	private boolean highlighted;
	private final WrappingScalableCanvas canvas;
	private HighlightedSelectionOverlay overlay;
	private Rectangle rect;
	
	public Selection(Point startPoint, Point endPoint,
			WrappingScalableCanvas canvas) {
		this.startPoint = startPoint;
		this.endPoint = endPoint;
		this.rect = new Rectangle(startPoint, new Dimension(endPoint.x - startPoint.x, endPoint.y - startPoint.y));
		this.canvas = canvas;
		overlay = new HighlightedSelectionOverlay(this, canvas);
	}

	public void setHighlighted(boolean highlighted) {
		if (highlighted) {
			canvas.subscribe(overlay);
		} else {
			canvas.unsubscribe(overlay);
		}
		this.highlighted = highlighted;
	}	

	public String getSelectionName() {
		return selectionName;
	}

	public void setSelectionName(String selectionName) {
		this.selectionName = selectionName;
		setChanged();
		notifyObservers();
	}


	public Point getStartPoint() {
		return startPoint;
	}

	public Point getEndPoint() {
		return endPoint;
	}

	public boolean isHighlighted() {
		return highlighted;
	}
	
	public Rectangle getRectangle() {
		return rect;
	}

	@Override
	public void paint(Graphics2D g2) {
		g2.setXORMode(new Color(0x505050));

		canvas.draw(new Rectangle(this.getStartPoint(), new Dimension(this
				.getEndPoint().x
				- this.getStartPoint().x, this.getEndPoint().y
				- this.getStartPoint().y)));
	}
}
