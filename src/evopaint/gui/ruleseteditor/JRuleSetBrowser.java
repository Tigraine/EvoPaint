/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.gui.ruleseteditor;

import evopaint.pixel.rulebased.interfaces.IRule;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.border.TitledBorder;

/**
 *
 * @author tam
 */
public class JRuleSetBrowser extends JList {

    public JRuleSetBrowser() {
        DefaultListModel listModel = new DefaultListModel();

        listModel.addElement("conway's game of life");
        listModel.addElement("colorful");
        listModel.addElement("lulz");
        
        setModel(listModel);
    }

}
