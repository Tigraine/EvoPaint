/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.gui.ruleseteditor;

import evopaint.Configuration;
import evopaint.pixel.rulebased.conditions.EmptyCondition;
import evopaint.pixel.rulebased.interfaces.ICondition;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 *
 * @author tam
 */
public class JConditionList extends JPanel {
    private List<ICondition> conditions;
    private List<JCondition> jConditions;

    public void removeCondition(JCondition jCondition) {
        jConditions.remove(jCondition);
        conditions.remove(jCondition.getCondition());
        remove(jCondition);
    }

    public void replaceCondition(JCondition old, JCondition fresh) { // yes I called it "new" at first...
        int i = jConditions.indexOf(old);
        jConditions.set(i, fresh);
        conditions.set(i, fresh.getCondition());
        add(fresh, i);
        remove(old);
    }

    public void collapseAll() {
        for (JCondition c : jConditions) {
            if (c.isExpanded()) {
                c.collapse();
            }
        }
        revalidate(); // we end up with a messy GUI if we don't revalidate here
    }

    public JConditionList(List<ICondition> conditions) {
        this.conditions = conditions;
        this.jConditions = new ArrayList(conditions.size());
        
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        //setLayout(new GridBagLayout());
        
        //setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED));
        
        for (ICondition condition : conditions) {
            JCondition c = new JCondition(condition, this);
            jConditions.add(c);
            //((GridLayout)getLayout()).setRows(((GridLayout)getLayout()).getRows() + 1);
            add(c);
            //revalidate();
        }

        Box andGoesLeft = Box.createHorizontalBox();
        JButton buttonAnd = new JButton("AND");
        buttonAnd.addActionListener(new AndButtonListener(this));
        andGoesLeft.add(buttonAnd);
        andGoesLeft.add(Box.createHorizontalGlue());
        add(andGoesLeft);
    }

    private class AndButtonListener implements ActionListener {
        private JConditionList jConditionList;

        public AndButtonListener(JConditionList jConditionList) {
            this.jConditionList = jConditionList;
        }
        
        public void actionPerformed(ActionEvent e) {
           // try {
                ICondition condition = new EmptyCondition(); // XXX maybe chose another? or place a placeholder?
                conditions.add(condition);
                JCondition jCondition = new JCondition(condition, jConditionList);
                jConditions.add(jCondition);
                jConditionList.add(jCondition, jConditions.size() - 1);
                //jConditionList.revalidate();
                //((GridLayout)getLayout()).setRows(((GridLayout)getLayout()).getRows() + 1);
                //SwingUtilities.getWindowAncestor(jConditionList).pack();
           /*} catch (InstantiationException ex) {
                ex.printStackTrace();
                System.exit(1);
                //Logger.getLogger(JConditionList.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                ex.printStackTrace();
                System.exit(1);
                //Logger.getLogger(JConditionList.class.getName()).log(Level.SEVERE, null, ex);
            }
            */
        }
    }
}
