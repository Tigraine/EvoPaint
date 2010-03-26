/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.gui.ruleseteditor;

import evopaint.pixel.rulebased.RuleSet;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

/**
 *
 * @author tam
 */
public class JRuleSetEditor extends JFrame {

    public JRuleSetEditor(RuleSet ruleSet) {
        setLayout(new BorderLayout(10, 10));

        // rule list
        JRuleList jRuleList = new JRuleList(ruleSet);
        JScrollPane scrollPaneForRuleList = new JScrollPane(jRuleList,
                JScrollPane.VERTICAL_SCROLLBAR_NEVER,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPaneForRuleList.setBorder(new TitledBorder("Rules"));
        scrollPaneForRuleList.setBackground(getBackground());
        add(scrollPaneForRuleList, BorderLayout.NORTH);

        add(new JRuleEditor(ruleSet.getRules().get(0)), BorderLayout.CENTER);

        JPanel panelButtons = new JPanel();
        add(panelButtons, BorderLayout.SOUTH);
        //panelButtons.setLayout(null);

        panelButtons.add(new JButton("Save & Close"));
        panelButtons.add(new JButton("Cancel"));

    }
}


