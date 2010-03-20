package evopaint.gui.listeners;

import evopaint.gui.TextInputWindow;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 20.03.2010
 * Time: 23:04:50
 * To change this template use File | Settings | File Templates.
 */
public class SelectionSetNameListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
        TextInputWindow window = new TextInputWindow();
        window.setVisible(true);
    }
}
