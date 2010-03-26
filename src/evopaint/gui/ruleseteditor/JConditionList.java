/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.gui.ruleseteditor;

import evopaint.pixel.rulebased.interfaces.ICondition;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.SoftBevelBorder;

/**
 *
 * @author tam
 */
public class JConditionList extends JPanel implements ActionListener {
    private List<ICondition> conditions;
    private List<JCondition> jConditions;

    public void actionPerformed(ActionEvent e) {
        for (JCondition c : jConditions) {
            if (c.isExpanded()) {
                c.collapse();
            }
        }

        ((JCondition)(((JButton)e.getSource()).getParent().getParent())).expand();
        revalidate(); // we end up with a messy GUI if we don't revalidate here
    }

    public JConditionList(List<ICondition> conditions) {
        this.conditions = conditions;
        this.jConditions = new ArrayList(conditions.size());
        
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        //setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED));
        
        for (ICondition condition : conditions) {
            JCondition c = new JCondition(condition, this);
            jConditions.add(c);
            add(c);
            //revalidate();
        }
    }
}
