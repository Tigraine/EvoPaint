package evopaint.gui;


import evopaint.Configuration;
import evopaint.EvoPaint;
import evopaint.commands.*;
import evopaint.gui.listeners.SelectionListenerFactory;
import evopaint.gui.rulesetmanager.JRuleSetManager;
import evopaint.pixel.rulebased.RuleSet;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.*;
import javax.swing.border.LineBorder;

public class MainFrame extends JFrame {
    private Container contentPane;
    private JPanel mainPanel;
    private MenuBar menuBar;
    private Showcase showcase;
    private JPopupMenu toolMenu;
    private JOptionsPanel jOptionsPanel;
    private ToolBox toolBox;
    private SelectionToolBox selectionToolBox;
    private PaintPanel jPixelPanel;
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
        this.contentPane = getContentPane();

        resumeCommand = new ResumeCommand(configuration);
        pauseCommand = new PauseCommand(configuration);
        zoomInCommand = new ZoomInCommand(showcase);
        zoomOutCommand = new ZoomOutCommand(showcase);

        activeTool = MoveCommand.class;
        CommandFactory commandFactory = new CommandFactory(configuration);

        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ee) {
                // mu!
            }
        }

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setLayout(new CardLayout());

        this.mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(0, 0));
        add(mainPanel, "main");
        this.jRuleSetManager = new JRuleSetManager(configuration,
                new RuleSetManagerOKListener(), new RuleSetManagerCancelListener());
        jRuleSetManager.setVisible(false);
        add(jRuleSetManager, "rule manager");

        this.toolMenu = new ToolMenu(this);
        this.jOptionsPanel = new JOptionsPanel(configuration); // FIXME: paintoptionspanel must be initialized before showcase or we get a nullpointer exception. the semantics to not express this!!
        this.showcase = new Showcase(configuration, this, evopaint.getWorld(), evopaint.getPerception(), commandFactory);
        this.menuBar = new MenuBar(configuration, evopaint, new SelectionListenerFactory(showcase), showcase);
        setJMenuBar(menuBar);

        addKeyListener(new MainFrameKeyListener());


        // some hand crafted GUI stuff for the panel on the left
        leftPanel = new JPanel();
        leftPanel.setLayout(new GridBagLayout());
        mainPanel.add(leftPanel, BorderLayout.WEST);

        JPanel wrapperPanelLeft = new JPanel();
        wrapperPanelLeft.setLayout(new GridBagLayout());
        wrapperPanelLeft.setBackground(new Color(0xF2F2F5));
        wrapperPanelLeft.setBorder(new LineBorder(Color.GRAY));
        GridBagConstraints constraintsWrapper = new GridBagConstraints();
        constraintsWrapper.anchor = GridBagConstraints.NORTHWEST;
        constraintsWrapper.insets = new Insets(3, 0, 3, 0);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(10, 10, 10, 10);
        leftPanel.add(wrapperPanelLeft, constraints);

        toolBox = new ToolBox(this);
        constraintsWrapper.gridy = 0;
        constraintsWrapper.fill = GridBagConstraints.HORIZONTAL;
        wrapperPanelLeft.add(toolBox, constraintsWrapper);
        
        JSeparator separator = new JSeparator(JSeparator.HORIZONTAL);
        constraintsWrapper.gridy++;
        constraintsWrapper.fill = GridBagConstraints.HORIZONTAL;
        wrapperPanelLeft.add(separator, constraintsWrapper);
        
        selectionToolBox = new SelectionToolBox(showcase);
        constraintsWrapper.gridy++;
        constraintsWrapper.fill = GridBagConstraints.NONE;
        wrapperPanelLeft.add(selectionToolBox, constraintsWrapper);

        separator = new JSeparator(JSeparator.HORIZONTAL);
        constraintsWrapper.gridy++;
        constraintsWrapper.fill = GridBagConstraints.HORIZONTAL;
        wrapperPanelLeft.add(separator, constraintsWrapper);

        jPixelPanel = new PaintPanel(configuration, new OpenRuleSetManagerListener());
        constraintsWrapper.gridy++;
        constraintsWrapper.fill = GridBagConstraints.NONE;
        wrapperPanelLeft.add(jPixelPanel, constraintsWrapper);

        separator = new JSeparator(JSeparator.HORIZONTAL);
        constraintsWrapper.gridy++;
        constraintsWrapper.fill = GridBagConstraints.HORIZONTAL;
        wrapperPanelLeft.add(separator, constraintsWrapper);

        constraintsWrapper.gridy++;
        constraintsWrapper.fill = GridBagConstraints.NONE;
        wrapperPanelLeft.add(jOptionsPanel, constraintsWrapper);

        JPanel wrapperPanelRight = new JPanel();
        wrapperPanelRight.setBackground(Color.WHITE);
        wrapperPanelRight.setLayout(new GridBagLayout());
        wrapperPanelRight.add(showcase);
        // and the right side
        JScrollPane showCaseScrollPane = new JScrollPane(wrapperPanelRight,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        // THIS IS NOT OBJECT ORIENTED! IT TOOK HOURS TO FIND THIS SHIT!
        showCaseScrollPane.setViewportBorder(null); 
        // showCaseScrollPane.getViewport().setBackground(new JPanel().getBackground());
        // ^ does not seem to work for viewports workaround v

        // not needed, but don't want to forget about: showCaseScrollPane.getViewport().setOpaque(false);

        // and if we do not set a null border we get a nice 1px solix black border "for free"
        // the JScrollPane is by far the most unpolished Swing component I have seen so far
        // edit: well that changed. SynthUI and JTree is my new favorite combo
        showCaseScrollPane.setBorder(new LineBorder(Color.GRAY));

        showCaseScrollPane.getViewport().addMouseWheelListener(showcase);
        mainPanel.add(showCaseScrollPane, BorderLayout.CENTER);

        //showCaseScrollPane.repaint();

        // XXX workaround to update size of toolmenu so it is displayed
        // at the correct coordinates later
        this.toolMenu.setVisible(true);
        this.toolMenu.setVisible(false);

        setPreferredSize(new Dimension(800, 600));

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
        jOptionsPanel.displayOptions(activeTool);
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
                if (configuration.runLevel < Configuration.RUNLEVEL_RUNNING) {
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
            RuleSet ruleSet = jRuleSetManager.getSelectedRuleSet();
            assert (ruleSet != null);
            //System.out.println(ruleSet);
            configuration.brush.setRuleSet(ruleSet);
            jPixelPanel.setRuleSetName(ruleSet.getName());
            ((CardLayout)contentPane.getLayout()).show(contentPane, "main");
            menuBar.setVisible(true);
            configuration.runLevel = Configuration.RUNLEVEL_RUNNING;
        }

    }

    private class RuleSetManagerCancelListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            ((CardLayout)contentPane.getLayout()).show(contentPane, "main");
            menuBar.setVisible(true);
            configuration.runLevel = Configuration.RUNLEVEL_RUNNING;
        }

    }

    private class OpenRuleSetManagerListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            ((CardLayout)contentPane.getLayout()).show(contentPane, "rule manager");
            menuBar.setVisible(false);
            configuration.runLevel = Configuration.RUNLEVEL_STOP;
        }
    }
}
