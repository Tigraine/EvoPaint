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
import evopaint.pixel.rulebased.targeting.QualifiedActionTarget;
import evopaint.pixel.rulebased.targeting.QualifiedTarget;
import evopaint.pixel.rulebased.targeting.Qualifier;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Markus Echterhoff <tam@edu.uni-klu.ac.at>
 */
public class JQualifiedTarget extends JPanel {
    private JMultiTarget jTarget;
    private JComboBox qualifierComboBox;

    public JQualifiedTarget(QualifiedTarget qualifiedTarget) {

        qualifierComboBox = new JComboBox();
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        for (Qualifier qualifier : Configuration.AVAILABLE_QUALIFIERS) {
            model.addElement(qualifier);
        }
        qualifierComboBox.setModel(model);
        qualifierComboBox.setRenderer(new NamedObjectListCellRenderer());
        Qualifier selection = null;
        for (Qualifier q : Configuration.AVAILABLE_QUALIFIERS) {
            if (q.getClass() == qualifiedTarget.getQualifier().getClass()) {
                selection = q;
            }
        }
        assert(selection != null);
        qualifierComboBox.setSelectedItem(selection);
        add(qualifierComboBox);

        JLabel ofLabel = new JLabel("in");
        add(ofLabel);

        jTarget = new JMultiTarget(qualifiedTarget);
        add(jTarget);
    }

    public QualifiedActionTarget createQualifiedActionTarget() {
        return new QualifiedActionTarget(jTarget.getDirections(),
                createQualifier());
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
