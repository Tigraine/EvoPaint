/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.gui;

import evopaint.commands.MoveCommand;
import evopaint.commands.PaintCommand;
import evopaint.commands.SelectCommand;

import java.awt.Cursor;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

/**
 *
 * @author tam
 */
public class ToolMenu extends JPopupMenu {
    private MainFrame mainFrame;

    public ToolMenu(MainFrame mf) {
        super();
        this.mainFrame = mf;
        //setOpaque(false);

        setLayout(new GridLayout(3,3));
        ArrayList<JMenuItem> tools = new ArrayList<JMenuItem>(9);
        for (int i = 0; i < 9; i++) {
            JMenuItem tool = new JMenuItem();
            tool.setText("tool"+i);
            tool.setBorderPainted(false);
            tools.add(tool);
            add(tool);
        }

        tools.get(0).setText("Move");
        tools.get(0).addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
            	
                mainFrame.setActiveTool(MoveCommand.class);
                mainFrame.setCursor(new Cursor(Cursor.MOVE_CURSOR));
            }

            /*// bugged
             menu item selection foobar: http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6515446

            public void mouseReleased(MouseEvent e) {

                selectedToolCommand = MoveCommand.class;
                System.out.println("selected move command");
            }*/
        });

        tools.get(1).setText("Paint");
        tools.get(1).addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
            	mainFrame.getShowcase().setpaintCommand();
            	
                mainFrame.setActiveTool(PaintCommand.class);
                mainFrame.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
        });

        tools.get(2).setText("Select");
        tools.get(2).addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                mainFrame.setActiveTool(SelectCommand.class);
                mainFrame.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
            }
        });

        tools.get(4).setVisible(false);
    }
}
