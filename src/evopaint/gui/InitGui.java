package evopaint.gui;

import evopaint.Config;
import evopaint.Entity;
import evopaint.Relation;
import evopaint.commands.PaintCommand;
import evopaint.commands.ZoomCommand;
import evopaint.entities.World;
import evopaint.interfaces.IAttribute; //import evopaint.EvoPaint.MyDraw;
import evopaint.attributes.PartsAttribute;
import evopaint.attributes.PixelPerceptionAttribute;
import evopaint.attributes.RelationsAttribute;
import evopaint.entities.Observer;
import evopaint.relations.PixelPerceptionRelation;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
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

	public InitGui() {

		Config.init();

		// create empty world
		List<Entity> parts = new ArrayList<Entity>();
		PartsAttribute pa = new PartsAttribute(parts);

		List<Relation> relations = new ArrayList<Relation>();
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
		initToolsComponents();
		this.pack();
		this.setVisible(true);
		this.work();
	}

	private void initToolsComponents() {
		// JPanel jPTest = new JPanel();
		// jPTest.setPreferredSize(new Dimension(100,100));
		GridLayout gl = new GridLayout(3, 3);

		JMenuItem tool1 = new JMenuItem();
		tool1.setText("tool1");
		tool1.setBorderPainted(false);
		
		JMenuItem tool2 = new JMenuItem();
		tool2.setText("paint");
		tool2.setBorderPainted(false);
		
		JMenuItem tool3 = new JMenuItem();
		tool3.setText("tool3");
		tool3.setBorderPainted(false);
		
		JMenuItem tool4 = new JMenuItem();
		tool4.setText("tool4");
		tool4.setBorderPainted(false);
		JMenuItem tool5 = new JMenuItem("");
		tool5.setBorderPainted(false);
		tool5.setBorderPainted(false);
		tool5.setVisible(false);

		JMenuItem tool6 = new JMenuItem();
		tool6.setText("tool6");
		tool6.setBorderPainted(false);
		JMenuItem tool7 = new JMenuItem();
		tool7.setText("tool7");
		tool7.setBorderPainted(false);
		JMenuItem tool8 = new JMenuItem();
		tool8.setText("tool8");
		tool8.setBorderPainted(false);
		JMenuItem tool9 = new JMenuItem();
		tool9.setText("tool9");
		tool9.setBorderPainted(false);
		jToolMenu = new JPopupMenu();
//		jToolMenu.setBorderPainted(false);
		jToolMenu.setLayout(gl);
		jToolMenu.add(tool1);
		jToolMenu.add(tool2);
		jToolMenu.add(tool3);
		jToolMenu.add(tool4);
		jToolMenu.add(tool5);
		jToolMenu.add(tool6);
		jToolMenu.add(tool7);
		jToolMenu.add(tool8);
		jToolMenu.add(tool9);
		
//		tool2.addMouseListener(new java.awt.event.MouseAdapter() {
//			public void mouseReleased(java.awt.event.MouseEvent evt) {
//				jPWorldMouseReleased(evt);
//			}
//		});

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
		jToolMenu = new MyPopupM();

		JMenuItem Fullscreen = new JMenuItem();
		JMenuItem HideTools = new JMenuItem();

		JMenuItem Help = new JMenuItem();
		JMenuItem Info = new JMenuItem();
		JMenuItem Tutorial = new JMenuItem();

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		GroupLayout jPWorldLayout = new GroupLayout(jPWorld);
		jPWorld.setLayout(jPWorldLayout);
		jPWorldLayout.setHorizontalGroup(jPWorldLayout.createParallelGroup(
				GroupLayout.Alignment.LEADING).addGap(0,
				Config.sizeX * Config.zoom, Short.MAX_VALUE));
		jPWorldLayout.setVerticalGroup(jPWorldLayout.createParallelGroup(
				GroupLayout.Alignment.LEADING).addGap(0,
				Config.sizeY * Config.zoom, Short.MAX_VALUE));

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

		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(
				GroupLayout.Alignment.LEADING).addComponent(jPWorld,
				GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
				Short.MAX_VALUE));
		layout.setVerticalGroup(layout.createParallelGroup(
				GroupLayout.Alignment.LEADING).addComponent(jPWorld,
				GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
				Short.MAX_VALUE));

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

	class MyPopupM extends JPopupMenu {
		public MyPopupM() {
			super();
			setOpaque(false);
		}
	}

	class MyMenuItem extends JMenuItem {
		public MyMenuItem(String text) {
			super(text);
			setOpaque(false);
		}

		public void paint(Graphics g) {
			Graphics2D g2 = (Graphics2D) g.create();
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
					0.5f));
			super.paint(g2);
			g2.dispose();
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
        ZoomCommand zoomCommand = new ZoomCommand(evt.getWheelRotation() * -1);
        zoomCommand.execute();
    }

	private void jPWorldMousePressed(java.awt.event.MouseEvent evt) {
		// TODO add your handling code here:

		if (evt.getButton() == MouseEvent.BUTTON1) {

            PaintCommand command = new PaintCommand(this.world, new Point(evt.getX(), evt.getY()), 20);
            command.execute();

        } else if (evt.getButton() == MouseEvent.BUTTON2) {

			System.out.println("EVENT MOUSE PRESSED Center");

		} else if (evt.getButton() == MouseEvent.BUTTON3) {

			System.out.println("EVENT MOUSE PRESSED Right:" + evt.getX() + ":"
					+ evt.getY());

			// jToolMenu.setLocation(evt.getX()+this.getX(),evt.getY()+this.getY());
			// menu.show(e.getComponent(), e.getX(), e.getY());
			jToolMenu.setVisible(true);
			jToolMenu.setVisible(false);
			jToolMenu.show(jPWorld,
					(evt.getX() - jToolMenu.getSize().width / 2),
					(evt.getY() - jToolMenu.getSize().height / 2));

			// jToolMenu.setVisible(true);

		}
	}

	private void jPWorldMouseReleased(java.awt.event.MouseEvent evt) {
		// TODO add your handling code here:

		if (evt.getButton() == MouseEvent.BUTTON3) {
			System.out.println("EVENT RIGHT MOUSE Released");
			jToolMenu.setVisible(false);
		}
	}

	// Variables declaration - do not modify
	private JMenu jFile;
	private JMenu jEdit;
	private JMenuBar jMenu;
	private MyDraw jPWorld;
	// End of variables declaration
	private JMenuItem Exit;
	private JPopupMenu jToolMenu;
	private JMenuItem Load;
	private JMenuItem NewEvo;
	private JMenuItem Export;
	private JMenuItem Save;
	private JMenuItem SaveAs;
}
