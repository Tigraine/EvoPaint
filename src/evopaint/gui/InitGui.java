package evopaint.gui;

import evopaint.Config;
import evopaint.Entity;
import evopaint.Relation;
import evopaint.entities.World;
import evopaint.interfaces.IAttribute;
//import evopaint.EvoPaint.MyDraw;
import evopaint.attributes.PartsAttribute;
import evopaint.attributes.PixelPerceptionAttribute;
import evopaint.attributes.RelationsAttribute;
import evopaint.entities.Observer;
import evopaint.relations.PixelPerceptionRelation;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.List;
import javax.swing.*;

public class InitGui extends JFrame {

	private World world;
	private Observer observer;
	private Relation perception;
	
	public InitGui(){
		
		Config.init();
		

		// create empty world
		List parts = new ArrayList<Entity>();
		PartsAttribute pa = new PartsAttribute(parts);

		List relations = new ArrayList<Relation>();
		RelationsAttribute ra = new RelationsAttribute(relations);
		long time = 0;
		this.world = new World(new IdentityHashMap<Class, IAttribute>(), pa,
				ra, time);
		this.world.init();

		// create observer
		PixelPerceptionAttribute ppa = new PixelPerceptionAttribute(
				Config.sizeX, Config.sizeY, BufferedImage.TYPE_INT_ARGB,
				Config.zoom);
		this.observer = new Observer(new IdentityHashMap<Class, IAttribute>(),
				ppa);

		// observe the world
		this.perception = new PixelPerceptionRelation(observer, world);

		this.setBackground(Color.WHITE);
		this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);

		initComponents();
		this.pack();
		this.setVisible(true);
		this.work();
	}
	
	private void initComponents() {

		jPWorld = new MyDraw();
		jMenu = new JMenuBar();
		jFile = new JMenu();
		jEdit = new JMenu();
		JMenu jHelp = new JMenu();
		
        NewEvo = new JMenuItem();    
        Save = new JMenuItem();
        SaveAs = new JMenuItem();       
        Load = new JMenuItem();       
        Export = new JMenuItem();       
        Exit = new JMenuItem();
        
        JMenuItem Fullscreen = new JMenuItem();
        JMenuItem HideTools = new JMenuItem();
        
        JMenuItem Help = new JMenuItem();
        JMenuItem Info = new JMenuItem();
        JMenuItem Tutorial = new JMenuItem();
		

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		GroupLayout jPWorldLayout = new GroupLayout(
				jPWorld);
		jPWorld.setLayout(jPWorldLayout);
		jPWorldLayout.setHorizontalGroup(jPWorldLayout.createParallelGroup(
				GroupLayout.Alignment.LEADING).addGap(0, Config.sizeX*Config.zoom,
				Short.MAX_VALUE));
		jPWorldLayout.setVerticalGroup(jPWorldLayout.createParallelGroup(
				GroupLayout.Alignment.LEADING).addGap(0, Config.sizeY*Config.zoom,
				Short.MAX_VALUE));

		jFile.setText("File");
		jMenu.add(jFile);

		jEdit.setText("Edit");
		jMenu.add(jEdit);
		
		jHelp.setText("?");
		jMenu.add(jHelp);
	
// FILE OPTIONS		
        NewEvo.setText("New Evo");
        jFile.add(NewEvo);
        
        Save.setText("Save");
        jFile.add(Save);
        
        SaveAs.setText("Save as");
        jFile.add(SaveAs);
        
        Load.setText("Load");
        jFile.add(Load);
        
        Export.setText("Export");
        jFile.add(Export);
        
        Exit.setText("Exit");
        jFile.add(Exit);
        
// EDIT OPTIONS
        Fullscreen.setText("Fullscreen");
        jEdit.add(Fullscreen);
        
        HideTools.setText("HideTools");
        jEdit.add(HideTools);
        
// HELP OPTIONS        
        Help.setText("Help");
        jHelp.add(Help);
        
        Tutorial.setText("Tutorial");
        jHelp.add(Tutorial);
        
        Info.setText("Info");
        jHelp.add(Info);

		setJMenuBar(jMenu);

		GroupLayout layout = new GroupLayout(
				getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(
				GroupLayout.Alignment.LEADING).addComponent(
				jPWorld, GroupLayout.DEFAULT_SIZE,
				GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
		layout.setVerticalGroup(layout.createParallelGroup(
				GroupLayout.Alignment.LEADING).addComponent(
				jPWorld, GroupLayout.DEFAULT_SIZE,
				GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));

		// EVENT DECLERATION
		jPWorld.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
			public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
				jPWorldMouseWheelMoved(evt);
			}
		});
		
        jPWorld.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
            	jPWorldMousePressed(evt);
            }
        });
        
        jPWorld.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jPWorldMouseReleased(evt);
            }
        });
        

   		
   		
		// pack();
	}
	
	public class MyDraw extends JPanel {
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(observer.getPerception(), 0, 0, null);
		}
	}
	
	public void work() {

		if (Config.nrRenderings == 0) {
			while (true) {
				this.world.step();
				boolean ret = this.perception
						.relate(Config.randomNumberGenerator);
				assert (ret);
				repaint();
			}
		} else {
			for (int i = 0; i < Config.nrRenderings; i++) {
				this.world.step();
				boolean ret = this.perception
						.relate(Config.randomNumberGenerator);
				assert (ret);
				repaint();
			}
		}
	}

	private void jPWorldMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
		// TODO add your handling code here:
		System.out.println("EVENT MOUSE WHEEL");

		
	}
	
    private void jPWorldMousePressed(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
		
		
		if (evt.getButton() == MouseEvent.BUTTON1) {

			System.out.println("EVENT MOUSE PRESSED Left");

			} else if (evt.getButton() == MouseEvent.BUTTON2) {

				System.out.println("EVENT MOUSE PRESSED Center");

			} else if (evt.getButton() == MouseEvent.BUTTON3) {

				System.out.println("EVENT MOUSE PRESSED Right");

			}
    }
    
    private void jPWorldMouseReleased(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
    	
    	if (evt.getButton() == MouseEvent.BUTTON3){
    		System.out.println("EVENT RIGHT MOUSE Released");
    	}
    }

	// Variables declaration - do not modify
	private JMenu jFile;
	private JMenu jEdit;
	private JMenuBar jMenu;
	private MyDraw jPWorld;
	// End of variables declaration
    private JMenuItem Exit;
    private JFrame jToolMenu;
    private JMenuItem Load;
    private JMenuItem NewEvo;
    private JMenuItem Export;
    private JMenuItem Save;
    private JMenuItem SaveAs;
}
