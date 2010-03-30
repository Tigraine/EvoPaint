package evopaint.gui;


import evopaint.Configuration;
import evopaint.EvoPaint;
import evopaint.commands.*;
import evopaint.gui.listeners.SelectionListenerFactory;
import evopaint.gui.ruleseteditor.JRuleSetManager;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.*;

public class MainFrame extends JFrame {
    private Container contentPane;
    private JPanel mainPanel;
    private MenuBar menuBar;
    private Showcase showcase;
    private JPopupMenu toolMenu;
    private PaintOptionsPanel paintOptionsPanel;
    private ToolBox toolBox;
    //private JPanel showCaseWrapper = new JPanel();
    private JPanel leftPanel;
    private JRuleSetManager jRuleSetManager;
    private Configuration configuration;

    private Class activeTool = null;
    private ResumeCommand resumeCommand;
    private PauseCommand pauseCommand;
    private ZoomCommand zoomOutCommand;
    private ZoomCommand zoomInCommand;

    public Configuration getConfiguration() {
        return configuration;
    }

    public MainFrame(Configuration configuration, EvoPaint evopaint) {
        this.configuration = configuration;
        initializeCommands(evopaint);
        this.contentPane = getContentPane();
        
        activeTool = MoveCommand.class;
        CommandFactory commandFactory = new CommandFactory(configuration);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch(Exception e) {
            // TODO handle exceptions
        }

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setLayout(new CardLayout());

        this.mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(0, 0));
        add(mainPanel, "main");
        this.jRuleSetManager = new JRuleSetManager(configuration.brush.getRuleSet(),
                configuration, new RuleSetManagerOKListener(), new RuleSetManagerCancelListener());
        jRuleSetManager.setVisible(false);
        add(jRuleSetManager, "rule manager");

        this.toolMenu = new ToolMenu(this);
        this.paintOptionsPanel = new PaintOptionsPanel(configuration, showcase, this, new OpenRuleSetManagerListener()); // FIXME: paintoptionspanel must be initialized before showcase or we get a nullpointer exception. the semantics to not express this!!
        this.showcase = new Showcase(configuration, this, evopaint.getWorld(), evopaint.getPerception(), commandFactory);
        this.menuBar = new MenuBar(evopaint, new SelectionListenerFactory(showcase), showcase);
        setJMenuBar(menuBar);

        addKeyListener(new MainFrameKeyListener());


        // some hand crafted GUI stuff for the panel on the left
        leftPanel = new JPanel();
        leftPanel.setBackground(getBackground());
        mainPanel.add(leftPanel, BorderLayout.WEST);

        JPanel wrapperPanelLeft = new JPanel();
        wrapperPanelLeft.setLayout(new BoxLayout(wrapperPanelLeft, BoxLayout.Y_AXIS));
        wrapperPanelLeft.setBackground(getBackground());
        leftPanel.add(wrapperPanelLeft);

        JPanel wrapperPanelToolBox = new JPanel();
        wrapperPanelToolBox.setBackground(getBackground());
        wrapperPanelLeft.add(wrapperPanelToolBox);
        
        toolBox = new ToolBox(this);
        wrapperPanelToolBox.add(toolBox);
        wrapperPanelLeft.add(Box.createVerticalStrut(10));
        wrapperPanelLeft.add(paintOptionsPanel);

        // and the right side
        JScrollPane showCaseScrollPane = new JScrollPane(showcase,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        showCaseScrollPane.setBackground(getBackground());

        // THIS IS NOT OBJECT ORIENTED! IT TOOK HOURS TO FIND THIS SHIT!
        showCaseScrollPane.setViewportBorder(null); 
        // showCaseScrollPane.getViewport().setBackground(new JPanel().getBackground());
        // ^ does not seem to work for viewports workaround v
        showCaseScrollPane.getViewport().setOpaque(false);
        // and if we do not set a null border we get a nice 1px solix black border "for free"
        // the JScrollPane is by far the most unpolished Swing component I have seen so far
        showCaseScrollPane.setBorder(null);

        showCaseScrollPane.getViewport().addMouseWheelListener(showcase);
        mainPanel.add(showCaseScrollPane, BorderLayout.CENTER);

        //showCaseScrollPane.repaint();

        // XXX workaround to update size of toolmenu so it is displayed
        // at the correct coordinates later
        this.toolMenu.setVisible(true);
        this.toolMenu.setVisible(false);

        //setPreferredSize(new Dimension(800, 600));

        this.pack();
        this.setVisible(true);
    }

    public JPopupMenu getToolMenu() {
        return toolMenu;
    }

    public ToolBox getToolBox() {
        return toolBox;
    }

    public Class getActiveTool() {
        return activeTool;
    }

    public void setActiveTool(Class activeTool) {
        this.activeTool = activeTool;
    }

    public void initializeCommands(EvoPaint evopaint) {
    	
        CommandFactory commandFactory = new CommandFactory(configuration);
        this.paintOptionsPanel = new PaintOptionsPanel(configuration, showcase, this, new OpenRuleSetManagerListener()); // FIXME: paintoptionspanel must be initialized before showcase or we get a nullpointer exception. the semantics to not express this!!
        this.showcase = new Showcase(configuration, this, evopaint.getWorld(), evopaint.getPerception(), commandFactory);
        this.menuBar = new MenuBar(evopaint, new SelectionListenerFactory(showcase), showcase);
        
        resumeCommand = new ResumeCommand(evopaint);
        pauseCommand = new PauseCommand(evopaint);
        zoomInCommand = new ZoomInCommand(showcase);
        zoomOutCommand = new ZoomOutCommand(showcase);
      
    }
    
 
    
    public void initSecond(EvoPaint evopaint){
 
        addKeyListener(new KeyListener() {

            public void keyTyped(KeyEvent e) {
               // System.out.println(""+e.getK)
                
                //System.out.println("adsf");
                //throw new UnsupportedOperationException("Not supported yet.");
            }

            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_PLUS) {
                    zoomInCommand.execute();
                } else if (e.getKeyCode() == KeyEvent.VK_MINUS) {
                    zoomOutCommand.execute();
                } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    if (configuration.running) {
                        pauseCommand.execute();
                    } else {
                        resumeCommand.execute();
                    }
                }
            }

            public void keyReleased(KeyEvent e) {
                //throw new UnsupportedOperationException("Not supported yet.");
            }
        });

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        setJMenuBar(menuBar);

        
        setLayout(new BoxLayout(this.getContentPane(), BoxLayout.X_AXIS));

       
        leftPanel.setLayout(new GridBagLayout());
        paintOptionsPanel.setAlignmentY(TOP_ALIGNMENT);
        leftPanel.add(paintOptionsPanel);
        toolBox.add(paintOptionsPanel);       


        // XXX workaround to update size of toolmenu so it is displayed
        // at the correct coordinates later
        this.toolMenu.setVisible(true);
        this.toolMenu.setVisible(false);

        this.pack();
        this.setVisible(true);
    }

    public Showcase getShowcase() {
        return showcase;
    }

    public void showToolMenu(Point location) {
        //System.out.println("click at " + location + " to " + (location.x - toolMenu.getSize().width / 2) + "/" + (location.y - toolMenu.getSize().height / 2));
        toolMenu.show(showcase,
                (location.x - toolMenu.getSize().width / 2),
                (location.y - toolMenu.getSize().height / 2));
    }

    public void hideToolMenu() {
        toolMenu.setVisible(false);
    }

    public void resize() {
        this.pack();
    }

    public PaintOptionsPanel getPop() {
        return paintOptionsPanel;
    }

    public void setConfiguration(Configuration conf) {
        this.configuration = conf;
    }

    private class MainFrameKeyListener implements KeyListener {

        public void keyTyped(KeyEvent e) {
            // System.out.println(""+e.getK)
            //System.out.println("adsf");
            //throw new UnsupportedOperationException("Not supported yet.");
        }

        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_PLUS) {
                zoomInCommand.execute();
            } else if (e.getKeyCode() == KeyEvent.VK_MINUS) {
                zoomOutCommand.execute();
            } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                if (configuration.running) {
                    pauseCommand.execute();
                } else {
                    resumeCommand.execute();
                }
            }
        }

        public void keyReleased(KeyEvent e) {
            //throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    private class RuleSetManagerOKListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            // TODO save rule
            ((CardLayout)contentPane.getLayout()).show(contentPane, "main");
        }

    }

    private class RuleSetManagerCancelListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            ((CardLayout)contentPane.getLayout()).show(contentPane, "main");
        }

    }

    private class OpenRuleSetManagerListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            ((CardLayout)contentPane.getLayout()).show(contentPane, "rule manager");
        }
    }
}
