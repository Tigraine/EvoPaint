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
import evopaint.pixel.rulebased.targeting.ActionMetaTarget;
import evopaint.pixel.rulebased.targeting.ActionTarget;
import evopaint.pixel.rulebased.targeting.IActionTarget;
import evopaint.pixel.rulebased.targeting.ITarget;
import evopaint.pixel.rulebased.targeting.MetaTarget;
import evopaint.pixel.rulebased.targeting.QualifiedMetaTarget;
import evopaint.pixel.rulebased.targeting.Qualifier;
import evopaint.pixel.rulebased.targeting.SingleTarget;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Markus Echterhoff <tam@edu.uni-klu.ac.at>
 */
public class JActionTargetPanel extends JPanel {
    private JTarget jTarget;
    private JComboBox qualifierComboBox;
    private boolean qualifierEnabled;
    private int selectedQualifierIndex;

    public JActionTargetPanel(ITarget target) {
        setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);

        qualifierComboBox = new JComboBox();
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        for (Qualifier qualifier : Configuration.AVAILABLE_QUALIFIERS) {
            model.addElement(qualifier);
        }
        qualifierComboBox.setModel(model);
        qualifierComboBox.setRenderer(new NamedObjectListCellRenderer());
        if (target instanceof QualifiedMetaTarget) {
            Qualifier selection = null;
            for (Qualifier q : Configuration.AVAILABLE_QUALIFIERS) {
                if (q.getClass() == ((QualifiedMetaTarget)target).getQualifier().getClass()) {
                    selection = q;
                }
            }
            assert(selection != null);
            qualifierComboBox.setSelectedItem(selection);
        }
        add(qualifierComboBox, c);

        final JLabel inLabel = new JLabel("in");
        c.gridx = 1;
        add(inLabel, c);

        jTarget = new JTarget(target, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (qualifierEnabled == false && jTarget.numSelected() > 1) {
                    qualifierComboBox.setSelectedIndex(selectedQualifierIndex);
                    qualifierComboBox.setEnabled(true);
                    inLabel.setEnabled(true);
                    qualifierEnabled = true;
                }
                else if (qualifierEnabled == true && jTarget.numSelected() <= 1) {
                    selectedQualifierIndex = qualifierComboBox.getSelectedIndex();
                    qualifierComboBox.setSelectedItem(null); // intentional. the named list renderer can deal with this and will display an empty label this is the cheapest way i could find to produce the desired effect of not displaying anything in the combo box
                    qualifierComboBox.setEnabled(false);
                    inLabel.setEnabled(false);
                    qualifierEnabled = false;
                }
            }
        });
        c.gridx = 2;
        add(jTarget, c);

        if (jTarget.numSelected() > 1) {
            qualifierComboBox.setEnabled(true);
            inLabel.setEnabled(true);
            qualifierEnabled = true;
        } else {
            qualifierComboBox.setSelectedItem(null);
            qualifierComboBox.setEnabled(false);
            inLabel.setEnabled(false);
            qualifierEnabled = false;
        }
    }

    public IActionTarget createActionTarget() {
        
        ITarget target = jTarget.getTarget();

        if (target == null) {
            return new ActionTarget();
        }

        if (target instanceof MetaTarget) {
            return new ActionMetaTarget(((MetaTarget)target).getDirections(),
                    createQualifier());
        }

        return new ActionTarget(((SingleTarget)target).getDirection());
    }

    private Qualifier createQualifier() {
        Qualifier qualifier = null;
        Qualifier prototype = (Qualifier)qualifierComboBox.getSelectedItem();
        try {
            qualifier = prototype.getClass().newInstance();
        } catch (InstantiationException ex) {
            ex.printStackTrace();
            System.exit(1);
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
            System.exit(1);
        }
        assert(qualifier != null);
        return qualifier;
    }
    
}
