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

package evopaint.pixel.rulebased.targeting.qualifiers;

import evopaint.Configuration;
import evopaint.gui.rulesetmanager.util.NamedObjectListCellRenderer;
import evopaint.pixel.Pixel;
import evopaint.pixel.rulebased.targeting.Qualifier;
import evopaint.pixel.rulebased.util.ObjectComparisonOperator;
import evopaint.util.mapping.RelativeCoordinate;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JComponent;

/**
 *
 * @author Markus Echterhoff <tam@edu.uni-klu.ac.at>
 */
public class ExistenceQualifier extends Qualifier {

    private ObjectComparisonOperator objectComparisonOperator;

    public ExistenceQualifier(ObjectComparisonOperator objectComparisonOperator) {
        this.objectComparisonOperator = objectComparisonOperator;
    }

    public ExistenceQualifier() {
        this.objectComparisonOperator = ObjectComparisonOperator.EQUAL;
    }

    public ObjectComparisonOperator getObjectComparisonOperator() {
        return objectComparisonOperator;
    }

    public void setObjectComparisonOperator(ObjectComparisonOperator objectComparisonOperator) {
        this.objectComparisonOperator = objectComparisonOperator;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ExistenceQualifier other = (ExistenceQualifier) obj;
        if (this.objectComparisonOperator != other.objectComparisonOperator && (this.objectComparisonOperator == null || !this.objectComparisonOperator.equals(other.objectComparisonOperator))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + (this.objectComparisonOperator != null ? this.objectComparisonOperator.hashCode() : 0);
        return hash;
    }


    public String getName() {
        return "existence";
    }

    public List<RelativeCoordinate> getCandidates(Pixel actor, List<RelativeCoordinate> directions, Configuration configuration) {
        List<RelativeCoordinate> ret = new ArrayList(1);
        for (RelativeCoordinate direction : directions) {
            Pixel target = configuration.world.get(actor.getLocation(), direction);
            if (false == objectComparisonOperator.compare(target, null)) {
                ret.add(direction);
            }
        }
        return ret;
    }

    @Override
    public String toString() {
        if (objectComparisonOperator == ObjectComparisonOperator.EQUAL) {
            return "is a pixel";
        }
        return "is a free spot";
    }

    @Override
    public String toHTML() {
        return toString();
    }

    @Override
    public LinkedHashMap<String, JComponent> addParametersGUI(LinkedHashMap<String, JComponent> parametersMap) {
        parametersMap = super.addParametersGUI(parametersMap);

        JComboBox comparisonComboBox = new JComboBox(ObjectComparisonOperator.createComboBoxModel());
        comparisonComboBox.setRenderer(new NamedObjectListCellRenderer());
        if (objectComparisonOperator == null) {
            objectComparisonOperator = ObjectComparisonOperator.EQUAL;
        }
        comparisonComboBox.setSelectedItem(objectComparisonOperator);
        comparisonComboBox.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                setObjectComparisonOperator((ObjectComparisonOperator) ((JComboBox) (e.getSource())).getSelectedItem());
            }
        });
        comparisonComboBox.setPreferredSize(new Dimension(80, 25));
        parametersMap.put("Comparison", comparisonComboBox);

        return parametersMap;
    }
    
}
