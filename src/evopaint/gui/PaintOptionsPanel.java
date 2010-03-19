package evopaint.gui;

import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeListenerProxy;
import java.util.IdentityHashMap;

import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JToggleButton;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.bric.swing.ColorPicker;

import net.java.dev.colorchooser.ColorChooser;

import evopaint.EvoPaint;
import evopaint.interfaces.IAttribute;

public class PaintOptionsPanel extends Panel{
	
	private int color;
	private int brushsize;
	private IdentityHashMap<Class,IAttribute> newAttributes = new IdentityHashMap<Class,IAttribute>();
	ColorChooser cc= null;
	JSlider jBrushSizeSlider=null; 
	Showcase sc;
	MainFrame mf;
	Checkbox cBRandom;
	
	public PaintOptionsPanel(final Showcase sc, final MainFrame mf){
		
		this.mf=mf;
		this.sc=sc;
		JPanel jColorPanel = new JPanel();
		JPanel jBrushPanel = new JPanel();
		JPanel jAttrPanel = new JPanel();
		JLabel jLrandom = new JLabel();
				
		jColorPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Color", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(0, 0, 0)));
		jBrushPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Brushsize", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(0, 0, 0)));
		jAttrPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Attributes", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(0, 0, 0)));
		
		myListener myL= new myListener(sc,mf);
		
		jLrandom.setText("Random Color");
		
		cBRandom = new Checkbox();
		

		cBRandom.addItemListener(myL);
		cc = new ColorChooser();
		jColorPanel.add(cc);
		jColorPanel.add(cBRandom);
		
		jBrushSizeSlider = new JSlider();
		jBrushPanel.add(jBrushSizeSlider);

	
	
		jBrushSizeSlider.addChangeListener(myL);


		
		cc.addActionListener(myL);
				
		this.setLayout(new BorderLayout());
		this.add(jColorPanel,BorderLayout.LINE_START);
		this.add(jBrushPanel,BorderLayout.CENTER);
		this.add(jAttrPanel,BorderLayout.LINE_END);
	}

	public int getBrushsize() {
		brushsize=jBrushSizeSlider.getValue();
		return brushsize;
	}

	public void setBrushsize(int brushsize) {
		this.brushsize = brushsize;
	}
	
	public int getColor() {
		color = cc.getColor().getRGB();
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public boolean getcBRandom() {
		return cBRandom.getState();
	}

	public void setcBRandom(boolean value) {		
		cBRandom.setState(value);
	}
	
	
	
}
