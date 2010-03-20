/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.gui.ruleeditor;

import evopaint.interfaces.ICondition;
import evopaint.interfaces.IRule;
import java.awt.Color;
import java.awt.Component;
import java.util.Iterator;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

/**
 *
 * @author tam
 */
public class JRule extends JPanel {
    private DefaultListModel ruleModel;

    JRule(IRule rule) {
        add(new KeywordLabel("IF"));
        add(Box.createHorizontalStrut(10));
        for (Iterator<ICondition> ii = rule.getConditions().iterator(); ii.hasNext();) {
            add(new NormalLabel(ii.next().toString()));
            if (ii.hasNext()) {
                add(Box.createHorizontalStrut(10));
                add(new KeywordLabel("AND"));
                add(Box.createHorizontalStrut(10));
            }
        }
        add(Box.createHorizontalStrut(10));
        add(new KeywordLabel("THEN"));
        add(Box.createHorizontalStrut(10));
        add(new NormalLabel(rule.getAction().toString()));
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    private class KeywordLabel extends JLabel {
        public KeywordLabel(String text) {
            super(text);
            setForeground(Color.red);
            
            addMouseListener(new java.awt.event.MouseAdapter() {

                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    setBackground(UIManager.getColor("control").brighter());
                }

                public void mouseExited(java.awt.event.MouseEvent evt) {
                   // setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED));
                    setBackground(UIManager.getColor("control"));
                }
            });

            //setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED));
        setOpaque(true);
        //set
        }
    }

    private class NormalLabel extends JLabel {
        public NormalLabel(String text) {
            super(text);
            setForeground(Color.black);
            //setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED));
        }
    }
}
