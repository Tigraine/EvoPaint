/*
 *  Copyright (C) 2010 Markus Echterhoff <tam@edu.uni-klu.ac.at>
 * 
 *  This file is part of EvoPaint.
 * 
 *  EvoPaint is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with EvoPaint.  If not, see <http://www.gnu.org/licenses/>.
 */

package evopaint.gui.rulesetmanager;

import evopaint.Configuration;
import evopaint.gui.rulesetmanager.util.NamedObjectListCellRenderer;
import evopaint.pixel.rulebased.targeting.IActionTarget;
import evopaint.pixel.rulebased.targeting.IConditionTarget;
import evopaint.pixel.rulebased.targeting.ITarget;
import evopaint.pixel.rulebased.targeting.QualifiedActionTarget;
import evopaint.pixel.rulebased.targeting.QualifiedTarget;
import evopaint.pixel.rulebased.targeting.QuantifiedConditionTarget;
import evopaint.pixel.rulebased.targeting.QuantifiedTarget;
import evopaint.pixel.rulebased.targeting.SpecifiedActionTarget;
import evopaint.pixel.rulebased.targeting.SpecifiedConditionTarget;
import evopaint.pixel.rulebased.targeting.SingleTarget;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 *
 * @author Markus Echterhoff <tam@edu.uni-klu.ac.at>
 */
public class JTargetButton extends JButton {
    public static final int CONDITION = 0;
    public static final int ACTION = 1;

    private Configuration configuration;
    private int type;
    private JDialog dialog;
    private JComboBox targetTypeComboBox;
    private JPanel targetPanel;
    private ITarget target;

    public JTargetButton(Configuration configuration, final ITarget passedTarget, final int type) {
        this.configuration = configuration;
        this.type = type;
        this.target = passedTarget;

        this.dialog = new JDialog((JFrame)SwingUtilities.getWindowAncestor(this), "Choose Target", true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setLayout(new GridBagLayout());
        dialog.setUndecorated(true);

        final GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.insets = new Insets(5, 5, 5, 5);

        DefaultComboBoxModel comboBoxModel = new DefaultComboBoxModel();
        if (type == ACTION) {
            for (IActionTarget t : Configuration.AVAILABLE_ACTION_TARGETS) {
                comboBoxModel.addElement(t);
            }
        }
        else if (type == CONDITION) {
            for (IConditionTarget t : Configuration.AVAILABLE_CONDITION_TARGETS) {
                comboBoxModel.addElement(t);
            }
        }
        else {
            assert (false);
        }
        targetTypeComboBox = new JComboBox(comboBoxModel);
        targetTypeComboBox.setPreferredSize(new Dimension(100, 25));
        targetTypeComboBox.setRenderer(new NamedObjectListCellRenderer());

        ITarget selection = null;
        if (type == ACTION) {
            for (IActionTarget t : Configuration.AVAILABLE_ACTION_TARGETS) {
                if (t.getClass() == target.getClass()) {
                    selection = t;
                }
            }
        }
        else if (type == CONDITION) {
            for (IConditionTarget t : Configuration.AVAILABLE_CONDITION_TARGETS) {
                if (t.getClass() == target.getClass()) {
                    selection = t;
                }
            }
        }
        else {
            assert (false);
        }
        assert(selection != null);
        targetTypeComboBox.setSelectedItem(selection);
        
        targetTypeComboBox.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (targetPanel != null) {
                    dialog.remove(targetPanel);
                }
                if (targetTypeComboBox.getSelectedItem() instanceof SingleTarget) {
                    if (type == ACTION) {
                        targetPanel = new JSpecifiedTarget(
                                new SpecifiedActionTarget());
                    }
                    else if (type == CONDITION) {
                        targetPanel = new JSpecifiedTarget(
                                new SpecifiedConditionTarget());
                    }
                }
                else if (targetTypeComboBox.getSelectedItem() instanceof QualifiedTarget) {
                    if (type == ACTION) {
                        targetPanel = new JQualifiedTarget(
                                new QualifiedActionTarget());
                    }
                    else if (type == CONDITION) {
                        throw new UnsupportedOperationException("It turned out qualified condition targets do not add new information but where just confusing.");
                    }
                }
                else if (targetTypeComboBox.getSelectedItem() instanceof QuantifiedTarget) {
                    if (type == ACTION) {
                        throw new UnsupportedOperationException("For the time being, action target quantification seems like a stupid idea to the designer of EvoPaint.");
                    }
                    else if (type == CONDITION) {
                        targetPanel = new JQuantifiedTarget(
                                new QuantifiedConditionTarget());
                    }
                }
                assert (targetPanel != null);
                GridBagConstraints c2 = new GridBagConstraints();
                c2.anchor = GridBagConstraints.CENTER;
                c2.fill = GridBagConstraints.BOTH;
                c2.weightx = 1;
                c2.gridy = 1;
                dialog.add(targetPanel, c2);
                dialog.pack();
            }
        });

        dialog.add(targetTypeComboBox, c);

        if (target instanceof SpecifiedActionTarget) {
            assert (type == ACTION);
            targetPanel = new JSpecifiedTarget((SpecifiedActionTarget)target);
        }
        else if (target instanceof SpecifiedConditionTarget) {
            assert (type == CONDITION);
            targetPanel = new JSpecifiedTarget((SpecifiedConditionTarget)target);
        }
        else if (target instanceof QualifiedActionTarget) {
            assert (type == ACTION);
            targetPanel = new JQualifiedTarget((QualifiedActionTarget)target);
        }
        else if (target instanceof QuantifiedConditionTarget) {
            assert (type == CONDITION);
            targetPanel = new JQuantifiedTarget((QuantifiedConditionTarget)target);
        }
        else {
            assert (false);
        }

        c.gridy = 1;
        dialog.add(targetPanel, c);

        JPanel controlPanel = new JPanel();
        final JButton okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
                target = null;
                target = createTarget();
                setText("<html>" + target.toHTML() + "</html>");
            }
         });
        controlPanel.add(okButton);
        final JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
         });
        controlPanel.add(cancelButton);
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 2;
        dialog.add(controlPanel, c);
        final JButton tis = this;
        addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dialog.pack();
                dialog.setLocationRelativeTo(tis);
                dialog.setVisible(true);
            }
        });

        setText("<html>" + target.toHTML() + "</html>");
    }

    public JTargetButton() {
    }

    public ITarget createTarget() {
        if (target != null) {
            return target;
        }
        if (targetPanel instanceof JSpecifiedTarget) {
            if (type == ACTION) {
                target = ((JSpecifiedTarget)targetPanel).createSpecifiedActionTarget();
            }
            else if (type == CONDITION) {
                target = ((JSpecifiedTarget)targetPanel).createSpecifiedConditionTarget();
            }
        }
        else if (targetPanel instanceof JQualifiedTarget) {
            if (type == ACTION) {
                target = ((JQualifiedTarget)targetPanel).createQualifiedActionTarget();
            }
            else if (type == CONDITION) {
                throw new UnsupportedOperationException("It turned out qualified condition targets do not add new information but where just confusing.");
            }
            
        }
        else if (targetPanel instanceof JQuantifiedTarget) {
            if (type == ACTION) {
                throw new UnsupportedOperationException("For the time being, action target quantification seems like a stupid idea to the designer of EvoPaint.");
            }
            else if (type == CONDITION) {
                target = ((JQuantifiedTarget)targetPanel).createQuantifiedConditionTarget();
            }
        }
        else {
            assert (false);
        }
        return target;
    }

}
