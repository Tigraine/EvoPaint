/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.gui.ruleseteditor;

import evopaint.pixel.rulebased.RuleSet;
import java.awt.BorderLayout;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

/**
 *
 * @author tam
 */
public class JRuleSetEditor extends JPanel {

    public JRuleSetEditor(RuleSet ruleSet) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        
        // rule list
        JRuleList jRuleList = new JRuleList(ruleSet);
        JScrollPane scrollPaneForRuleList = new JScrollPane(jRuleList,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPaneForRuleList.setBorder(new TitledBorder("Rules"));
        scrollPaneForRuleList.setBackground(getBackground());
        add(scrollPaneForRuleList);

        JRuleEditor jRuleEditor = new JRuleEditor(ruleSet.getRules().get(0));
        //JScrollPane scrollPaneForRuleEditor = new JScrollPane(jRuleEditor,
        //        JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
        //        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        //scrollPaneForRuleEditor.setBorder(new TitledBorder("Edit Rule"));
        //scrollPaneForRuleEditor.setBackground(getBackground());
        //scrollPaneForRuleEditor.setWheelScrollingEnabled(true);
        add(jRuleEditor);
    }
}


