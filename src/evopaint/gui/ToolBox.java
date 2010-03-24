package evopaint.gui;

import java.awt.*;

import evopaint.commands.MoveCommand;
import evopaint.commands.PaintCommand;
import javax.swing.*;
import javax.swing.border.EtchedBorder;


import evopaint.commands.MoveCommand;
import evopaint.commands.PaintCommand;
import java.awt.Cursor;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;


public class ToolBox extends JPanel {
	
    FlowLayout buttonFl = new FlowLayout();
    GridLayout toolGl = new GridLayout(2,4);
    GridLayout panels = new GridLayout(2,1);
    
    JPanel jPanelTools = new JPanel();
    JPanel toolOptions;
    
    public void setToolOptions(JPanel toolOptions) {
		this.toolOptions = toolOptions;
	}

	JPanel toolOptionsMove = new JPanel();
    JPanel toolOptionsPaint = new JPanel();
    JPanel toolOptionsPick = new JPanel();
    JPanel toolOptionsSelect = new JPanel();
    JPanel toolOptionsZoom = new JPanel();
    JPanel toolOptionsGravitation = new JPanel();
    JPanel toolOptionsFill = new JPanel();

    JToggleButton jButtonMove = new JToggleButton();
    JToggleButton jButtonPaint = new JToggleButton();
    JToggleButton jButtonPick = new JToggleButton();
    JToggleButton jButtonSelect = new JToggleButton();
    JToggleButton jButtonZoom = new JToggleButton();
    JToggleButton jButtonGravitation= new JToggleButton();
    JToggleButton jButtonFill = new JToggleButton();
    
    private MainFrame mf=null;
  
    public ToolBox(final MainFrame mf){
     	super();
    	this.mf=mf;
    	toolOptions=mf.getPop();
    	
    	ImageIcon iconMove = new ImageIcon("..\\EvoPaint_merge\\graphics\\move.png");
    	ImageIcon iconPaint = new ImageIcon("..\\EvoPaint_merge\\graphics\\brush.png");
    	ImageIcon iconPick = new ImageIcon("..\\EvoPaint_merge\\graphics\\picker.png");
    	ImageIcon iconSelect = new ImageIcon("..\\EvoPaint_merge\\graphics\\select.png");
    	ImageIcon iconZoom = new ImageIcon("..\\EvoPaint_merge\\graphics\\zoom.png");
    	ImageIcon iconGravitation = new ImageIcon("..\\EvoPaint_merge\\graphics\\grav.png");
    	ImageIcon iconFill = new ImageIcon("..\\EvoPaint_merge\\graphics\\fill.png");
    	
    
    	

    	

    	jButtonMove.setIcon(iconMove);
    	jButtonMove.setBorderPainted(false);
       
//    	jButtonMove.addMouseListener(new java.awt.event.MouseAdapter() {
//
//            public void mouseEntered(MouseEvent e) {
//                mf.setActiveTool(MoveCommand.class);
//                mf.setCursor(new Cursor(Cursor.MOVE_CURSOR));
//            }
//        });
   	jButtonMove.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent e) {
          if(jButtonMove.isSelected()){
          	mf.setActiveTool(MoveCommand.class);
              mf.setCursor(new Cursor(Cursor.MOVE_CURSOR));
              jButtonPaint.setSelected(false);
         }
      }
  });

    		
    	
    	
    	jButtonPaint.setIcon(iconPaint);
    	jButtonPaint.setBorderPainted(false);
//    	
//    	jButtonPaint.addMouseListener(new java.awt.event.MouseAdapter() {
//
//            public void mouseEntered(MouseEvent e) {
//              mf.setActiveTool(PaintCommand.class);
//              mf.setCursor(new Cursor(Cursor.HAND_CURSOR));
//            }
//        });
    	jButtonPaint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                if(jButtonPaint.isSelected()){
				      mf.setActiveTool(PaintCommand.class);
				      mf.setCursor(new Cursor(Cursor.HAND_CURSOR));
				      jButtonMove.setSelected(false);
               }
            }
        });
    	jButtonPick.setIcon(iconPick);
    	jButtonPick.setBorderPainted(false);
    	jButtonSelect.setIcon(iconSelect);
    	jButtonSelect.setBorderPainted(false);
    	jButtonZoom.setIcon(iconZoom);
    	jButtonZoom.setBorderPainted(false);
    	jButtonGravitation.setIcon(iconGravitation);
    	jButtonGravitation.setBorderPainted(false);
    	jButtonFill.setIcon(iconFill);
    	jButtonFill.setBorderPainted(false);
        	
    	jPanelTools.add(jButtonMove);
    	jPanelTools.add(jButtonPaint); 
    	jPanelTools.add(jButtonPick);
    	jPanelTools.add(jButtonSelect); 
    	jPanelTools.add(jButtonZoom);
    	jPanelTools.add(jButtonGravitation); 
    	jPanelTools.add(jButtonFill);
    	
    	this.setLayout(panels);
    	this.add(jPanelTools);
    	this.add(toolOptions);
    	
    	toolOptions.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
    

    	jPanelTools.setLayout(toolGl);
    	
    	
    	this.setVisible(true);
    	
    }
    
    public JPanel getPanel(){
    	return this;
    }
}
