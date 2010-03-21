package evopaint.gui;


import evopaint.Configuration;
import evopaint.EvoPaint;
import evopaint.commands.*;
import evopaint.gui.listeners.SelectionListenerFactory;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.*;
import javax.swing.Box;

public class MainFrame extends JFrame {

    public MenuBar menuBar;
    private Showcase showcase;
    private JPopupMenu toolMenu;
    private PaintOptionsPanel paintOptionsPanel;

    private Configuration configuration;

    private Class activeTool = null;
    private ResumeCommand resumeCommand;
    private PauseCommand pauseCommand;
    private ZoomCommand zoomOutCommand;
    private ZoomCommand zoomInCommand;

    public JPopupMenu getToolMenu() {
        return toolMenu;
    }

    public Class getActiveTool() {
        return activeTool;
    }

    public void setActiveTool(Class activeTool) {
        this.activeTool = activeTool;
    }
    
    public MainFrame(Configuration configuration, EvoPaint evopaint) {
        this.configuration = configuration;

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch(Exception e) {
            // TODO handle exceptions
        }

        setDefaultCloseOperation(this.EXIT_ON_CLOSE);
        
        activeTool = MoveCommand.class;

        this.toolMenu = new ToolMenu(this);

        CommandFactory commandFactory = new CommandFactory(configuration);
        this.paintOptionsPanel = new PaintOptionsPanel(showcase,this); // FIXME: paintoptionspanel must be initialized before showcase or we get a nullpointer exception. the semantics to not express this!!
        this.showcase = new Showcase(configuration, this, evopaint.getWorld(), evopaint.getPerception(), commandFactory);
        this.menuBar = new MenuBar(evopaint, new SelectionListenerFactory(showcase));
        commandFactory.GetSelectCommand().addSelectionListener(menuBar);

        initializeCommands(evopaint);

        addKeyListener(new MainFrameKeyListener());

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        setJMenuBar(menuBar);



        JPanel showCaseWrapper = new JPanel();
        showCaseWrapper.setLayout(new GridBagLayout());
        showCaseWrapper.add(showcase);

        setLayout(new BoxLayout(this.getContentPane(), BoxLayout.X_AXIS));

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new GridBagLayout());
        paintOptionsPanel.setAlignmentY(TOP_ALIGNMENT);
        leftPanel.add(paintOptionsPanel);
        add(leftPanel);
        add(showCaseWrapper);


        // XXX workaround to update size of toolmenu so it is displayed
        // at the correct coordinates later
        this.toolMenu.setVisible(true);
        this.toolMenu.setVisible(false);

        this.pack();
        this.setVisible(true);
    }

    private void initializeCommands(EvoPaint evopaint) {
        resumeCommand = new ResumeCommand(evopaint);
        pauseCommand = new PauseCommand(evopaint);
        zoomInCommand = new ZoomInCommand(showcase);
        zoomOutCommand = new ZoomOutCommand(showcase);
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
                if (configuration.isRunning()) {
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
}
