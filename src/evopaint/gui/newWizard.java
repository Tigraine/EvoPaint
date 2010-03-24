package evopaint.gui;


import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import evopaint.EvoPaint;


public class newWizard extends JFrame{
	private MenuBar mb;
	private JTextField xTf;
	private JTextField yTf;
	
	public newWizard(final MenuBar mb){
		this.mb=mb;
		final JFrame jf= this;
		String description="Relevant zb bei Regeln";
		
		
		//initialize Components
		JPanel descriptionPanel = new JPanel();
		JPanel contentPanel = new JPanel();
		JPanel decisionPanel = new JPanel();
		
		xTf = new JTextField("0");
		yTf = new JTextField("0");
		JLabel xL = new JLabel("X-Size");
		JLabel yL = new JLabel("Y-Size");
		
		JLabel des = new JLabel(description);
				
		JButton ok = new JButton("Ok");
		JButton cancel = new JButton("Cancel");
		
		descriptionPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Description", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(0, 0, 0))); // NOI18N
		contentPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Size", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(0, 0, 0))); // NOI18N
	
		
		//set Layout
		this.setLayout(new BorderLayout());		
		contentPanel.setLayout(new GridLayout(2, 2, 5, 50));
		
		//add Components
		
		descriptionPanel.add(des);
		
		contentPanel.add(xL);
		xTf.setSize(150, 10);
		contentPanel.add(xTf);
		contentPanel.add(yL);
		contentPanel.add(yTf);
		
		decisionPanel.add(ok);
		decisionPanel.add(cancel);
		
		this.add(descriptionPanel,BorderLayout.PAGE_START);
		this.add(contentPanel,BorderLayout.CENTER);
		this.add(decisionPanel,BorderLayout.PAGE_END);
		
		
		this.setTitle("New Wizard");	
		this.setSize(250, 250);
		this.setAlwaysOnTop(true);
				
		this.setVisible(true);
		
		
		ok.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				jf.setVisible(false);
				mb.newEvolution(Integer.parseInt(xTf.getText()),Integer.parseInt(yTf.getText()));
			}
		});
		
		cancel.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				jf.setVisible(false);
			}
		});
		
	}
	
	
	
}
