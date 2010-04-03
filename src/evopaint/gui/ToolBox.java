package evopaint.gui;


import evopaint.commands.MoveCommand;
 
import evopaint.commands.PaintCommand;
import evopaint.commands.SelectCommand;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.border.BevelBorder;



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
        JPanel container = new JPanel();
        container.setBackground(new Color(0xF2F2F5));
        add(container);
        container.setLayout(new GridLayout(0, 3, 3, 3));
        setBackground(new Color(0xF2F2F5));

        for (int i = 0; i < 7; i++) {
            JToggleButton btn = new JToggleButton();
            buttons.add(btn);
            btn.setPreferredSize(new Dimension(35, 35));
            btn.setMinimumSize(new Dimension(35, 35));
            btn.setMaximumSize(new Dimension(35, 35));
            container.add(btn);
        }

        final JToggleButton jButtonMove = buttons.get(0);
        
        jButtonMove.setIcon(new ImageIcon(getClass().getResource("icons/move.png")));
        jButtonMove.setSelected(true);
        jButtonMove.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent e) {
                    mf.setActiveTool(MoveCommand.class);
                    mf.getShowcase().setCursor(new Cursor(Cursor.MOVE_CURSOR));
                    select(jButtonMove);
            }
        });

        final JToggleButton jButtonPaint = buttons.get(1);
       
    	jButtonPaint.setIcon(new ImageIcon(getClass().getResource("icons/paint.png")));
        jButtonPaint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                  mf.setActiveTool(PaintCommand.class);
                  mf.getShowcase().setCursor(new Cursor(Cursor.HAND_CURSOR));
                select(jButtonPaint);
            }
        });

        final JToggleButton jButtonSelect = buttons.get(2);

        jButtonSelect.setIcon(new ImageIcon(getClass().getResource("icons/select.png")));
        jButtonSelect.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent e) {
                mf.setActiveTool(SelectCommand.class);
                mf.getShowcase().setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
                select(jButtonSelect);
            }
        });

        final JToggleButton jButtonPick = buttons.get(3);
        jButtonPick.setIcon(new ImageIcon(getClass().getResource("icons/pick.png")));
        jButtonPick.setEnabled(false);
        jButtonPick.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent e) {
                mf.setActiveTool(MoveCommand.class);
                mf.getShowcase().setCursor(new Cursor(Cursor.MOVE_CURSOR));
                select(jButtonPick);
            }
        });

        final JToggleButton jButtonZoom = buttons.get(4);
        jButtonZoom.setIcon(new ImageIcon(getClass().getResource("icons/zoom.png")));
        jButtonZoom.setEnabled(false);
        jButtonZoom.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent e) {
                mf.setActiveTool(MoveCommand.class);
                mf.getShowcase().setCursor(new Cursor(Cursor.MOVE_CURSOR));
                select(jButtonZoom);
            }
        });

        final JToggleButton jButtonFill = buttons.get(5);
        jButtonFill.setIcon(new ImageIcon(getClass().getResource("icons/fill.png")));
        jButtonFill.setEnabled(false);
        jButtonFill.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent e) {
                mf.setActiveTool(MoveCommand.class);
                mf.getShowcase().setCursor(new Cursor(Cursor.MOVE_CURSOR));
                select(jButtonFill);
            }
        });

        final JToggleButton jButtonErase = buttons.get(6);
        jButtonErase.setIcon(new ImageIcon(getClass().getResource("icons/erase.png")));
        jButtonErase.setEnabled(false);
        jButtonErase.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent e) {
                mf.setActiveTool(MoveCommand.class);
                mf.getShowcase().setCursor(new Cursor(Cursor.MOVE_CURSOR));
                select(jButtonErase);
            }
        });
    }
    
    private byte[] resourceToByteArray(String resName) throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        InputStream           iresStream = getClass().getResourceAsStream(resName);
        int                   bytesRead = 0;
        byte[]                tmp = new byte[4096];

        while ( (bytesRead = iresStream.read(tmp, 0, 4096)) > -1 )
          byteStream.write(tmp, 0, bytesRead);
           
        iresStream.close();
        // JDocs: "Closing a ByteArrayOutputStream has no effect."

        return byteStream.toByteArray();
      } 
}
