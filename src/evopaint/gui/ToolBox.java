package evopaint.gui;


import evopaint.commands.MoveCommand;
import evopaint.commands.PaintCommand;
import evopaint.commands.SelectCommand;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;


public class ToolBox extends JPanel {

    private List<JToggleButton> buttons = new ArrayList<JToggleButton>(8);

    private void select(JToggleButton selectedButton) {
        for (JToggleButton btn : buttons) {
            if (btn.isSelected() && btn != selectedButton) {
                btn.setSelected(false);
                return; // only one button is selected at any given time
            }
        }
    }
  
    public ToolBox(final MainFrame mf) {
        setLayout(new GridLayout(0, 3, 3, 3));
        setBorder(new LineBorder(Color.GRAY));

        for (int i = 0; i < 7; i++) {
            JToggleButton btn = new JToggleButton(Integer.toString(i));
            buttons.add(btn);
            btn.setPreferredSize(new Dimension(35, 35));
            add(btn);
        }

        final JToggleButton jButtonMove = buttons.get(0);
        jButtonMove.setText("M");
        jButtonMove.setIcon(new ImageIcon("..\\EvoPaint_new\\EvoPaint\\graphics\\move.png"));
        jButtonMove.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent e) {
                    mf.setActiveTool(MoveCommand.class);
                    mf.getShowcase().setCursor(new Cursor(Cursor.MOVE_CURSOR));
                    select(jButtonMove);
            }
        });

        final JToggleButton jButtonPaint = buttons.get(1);
        jButtonPaint.setText("P");
    	jButtonPaint.setIcon(new ImageIcon("..\\EvoPaint_new\\EvoPaint\\graphics\\brush.png"));
        jButtonPaint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                  mf.setActiveTool(PaintCommand.class);
                  mf.getShowcase().setCursor(new Cursor(Cursor.HAND_CURSOR));
                select(jButtonPaint);
            }
        });

        final JToggleButton jButtonSelect = buttons.get(2);
        jButtonSelect.setText("S");
        jButtonSelect.setIcon(new ImageIcon("..\\EvoPaint_new\\EvoPaint\\graphics\\select.png"));
        jButtonSelect.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent e) {
                mf.setActiveTool(SelectCommand.class);
                mf.getShowcase().setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
                select(jButtonSelect);
            }
        });

        final JToggleButton jButtonPick = buttons.get(3);
        jButtonPick.setIcon(new ImageIcon("..\\EvoPaint_new\\EvoPaint\\graphics\\picker.png"));
        jButtonPick.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent e) {
                mf.setActiveTool(MoveCommand.class);
                mf.getShowcase().setCursor(new Cursor(Cursor.MOVE_CURSOR));
                select(jButtonPick);
            }
        });

        final JToggleButton jButtonZoom = buttons.get(4);
        jButtonZoom.setIcon(new ImageIcon("..\\EvoPaint_new\\EvoPaint\\graphics\\zoom.png"));
        jButtonZoom.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent e) {
                mf.setActiveTool(MoveCommand.class);
                mf.getShowcase().setCursor(new Cursor(Cursor.MOVE_CURSOR));
                select(jButtonZoom);
            }
        });

        final JToggleButton jButtonFill = buttons.get(5);
        jButtonFill.setIcon(new ImageIcon("..\\EvoPaint_new\\EvoPaint\\graphics\\fill.png"));
        jButtonFill.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent e) {
                mf.setActiveTool(MoveCommand.class);
                mf.getShowcase().setCursor(new Cursor(Cursor.MOVE_CURSOR));
                select(jButtonFill);
            }
        });

        final JToggleButton jButtonErase = buttons.get(6);
        jButtonErase.setIcon(new ImageIcon("..\\EvoPaint_new\\EvoPaint\\graphics\\erase.png"));
        jButtonErase.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent e) {
                mf.setActiveTool(MoveCommand.class);
                mf.getShowcase().setCursor(new Cursor(Cursor.MOVE_CURSOR));
                select(jButtonErase);
            }
        });
    }
}
