package evopaint.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.InputMethodEvent;
import java.awt.event.InputMethodListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;

import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class myListener implements ActionListener, ChangeListener, ItemListener{
	
	Showcase sc;
	MainFrame mf;
	
	public myListener(Showcase sc, MainFrame mf){
		this.sc=sc;
		this.mf=mf;
	}
	
	@Override
	public void stateChanged(ChangeEvent e) {
		// TODO Auto-generated method stub
		mf.getPop().setBrushsize(mf.getPop().getBrushsize());
		sc.setpaintCommand();
	
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		mf.getPop().setColor(mf.getPop().getColor());
		sc.setpaintCommand();
		
	}

	@Override
	public void itemStateChanged(ItemEvent arg0) {
		// TODO Auto-generated method stub
		if(mf.getPop().getcBRandom()){
			mf.getPop().cc.setEnabled(false);
		}else{
			mf.getPop().cc.setEnabled(true);
		}
	}



}
