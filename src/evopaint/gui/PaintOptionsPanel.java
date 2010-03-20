package evopaint.gui;

import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.Panel;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

import com.bric.swing.ColorPicker;
import evopaint.gui.ruleeditor.JRuleEditor;
import net.java.dev.colorchooser.ColorChooser;



public class PaintOptionsPanel extends Panel{
	
	private int color;
	private int brushsize;
        private JRuleEditor ruleEditor;
//	private IdentityHashMap<Class,IAttribute> newAttributes = new IdentityHashMap<Class,IAttribute>();
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

                this.ruleEditor = new JRuleEditor();

		this.setLayout(new BorderLayout());
		this.add(jColorPanel,BorderLayout.LINE_START);
		this.add(jBrushPanel,BorderLayout.CENTER);
		this.add(jAttrPanel,BorderLayout.LINE_END);
                this.add(ruleEditor,BorderLayout.PAGE_END);
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
