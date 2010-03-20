package evopaint.gui;


import evopaint.EvoPaint;
import evopaint.Perception;
import evopaint.commands.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.*;

public class MainFrame extends JFrame {

    public JMenuBar menuBar;
    private Showcase showcase;
    private JPopupMenu toolMenu;
    private PaintOptionsPanel pop;

    private Class activeTool = null;
    private ResumeCommand resumeCommand;
    private PauseCommand pauseCommand;
    private ZoomCommand zoomOutCommand;
    private ZoomCommand zoomInCommand;

    public Class getActiveTool() {
        return activeTool;
    }

    public void setActiveTool(Class activeTool) {
        this.activeTool = activeTool;
    }

    public JPopupMenu getToolMenu() {
        return toolMenu;
    }
    
    public MainFrame(final EvoPaint evopaint) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch(Exception e) {
            // TODO handle exceptions
        }


        this.setBackground(Color.WHITE);
        this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
        
        this.activeTool = MoveCommand.class;

        this.toolMenu = new ToolMenu(this);
        this.menuBar = new MenuBar(this, evopaint);
        this.showcase = new Showcase(this, evopaint.getWorld(), evopaint.getPerception());
        this.pop = new PaintOptionsPanel(showcase,this);

        initializeCommands(evopaint);

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
                    if (evopaint.isRunning()) {
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

        BorderLayout layout = new BorderLayout();
        layout.addLayoutComponent(showcase, BorderLayout.CENTER);
        layout.addLayoutComponent(pop, BorderLayout.PAGE_END);
        getContentPane().add(showcase);
        getContentPane().add(pop);
        getContentPane().setLayout(layout);

        
       /* layout.setHorizontalGroup(layout.createParallelGroup(
                GroupLayout.Alignment.LEADING).addComponent(showcase,
                GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
                Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(
                GroupLayout.Alignment.LEADING).addComponent(showcase,
                GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
                Short.MAX_VALUE));
       */
        // XXX workaround to update size of toolmenu so it is displayed
        // at the correct coordinates later
        this.toolMenu.setVisible(true);
        this.toolMenu.setVisible(false);
//        add(pop);
//        add(showcase);
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
		return pop;
	}

}
