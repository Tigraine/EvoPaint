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

import com.thoughtworks.xstream.XStreamException;
import evopaint.Configuration;
import evopaint.gui.rulesetmanager.util.NamedObjectListCellRenderer;
import evopaint.pixel.rulebased.RuleSet;
import evopaint.util.CollectionNode;
import evopaint.util.ExceptionHandler;
import evopaint.util.RuleSetNode;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

/**
 *
 * @author Markus Echterhoff <tam@edu.uni-klu.ac.at>
 */
public class JImportRuleSetDialog extends JDialog {

    private Configuration configuration;
    private JRuleSetTree jRuleSetTree;
    private JComboBox collectionComboBox;
    private JTextArea messageTextArea;
    private JTextArea editorTextArea;

    public JImportRuleSetDialog(Configuration configurationArg, Component owner, JRuleSetTree jRuleSetTree, String xml) {
        this(configurationArg, owner, jRuleSetTree);
        editorTextArea.setText(xml);
    }

    public JImportRuleSetDialog(Configuration configurationArg, Component owner, JRuleSetTree jRuleSetTree) {
        super((JFrame)SwingUtilities.getWindowAncestor(owner),
                    "Import Rule Set", true);
        
        this.configuration = configurationArg;
        this.jRuleSetTree = jRuleSetTree;
        setLayout(new BorderLayout(10, 10));
        setPreferredSize(new Dimension(500, 400));

        JPanel collectionPanel = new JPanel();
        collectionPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        collectionPanel.add(new JLabel("Add to collection: "));


        DefaultMutableTreeNode root = (DefaultMutableTreeNode)
                jRuleSetTree.getModel().getRoot();
        Enumeration children = root.children();
        if (children == null) {
            ExceptionHandler.handle(new Exception("You will have to create a collection before you import rule sets"), false);
            return;
        }
        DefaultComboBoxModel comboBoxModel = new DefaultComboBoxModel();
        while (children.hasMoreElements()) {
            CollectionNode collectionNode =
                    (CollectionNode)children.nextElement();
            comboBoxModel.addElement(collectionNode);
        }
        collectionComboBox = new JComboBox(comboBoxModel);
        collectionComboBox.setRenderer(new NamedObjectListCellRenderer());
        collectionPanel.add(collectionComboBox);

        add(collectionPanel, BorderLayout.NORTH);


        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        JLabel editorLabel = new JLabel("Paste the rule set you wish to import below");
        editorLabel.setBorder(new LineBorder(centerPanel.getBackground(), 12));
        editorLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        centerPanel.add(editorLabel);

        editorTextArea = new JTextArea();
        editorTextArea.setBorder(null);
        JScrollPane scrollPane = new JScrollPane(editorTextArea,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(new LineBorder(new JPanel().getBackground(), 10));
        scrollPane.setViewportBorder(new BevelBorder(BevelBorder.LOWERED));

        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        centerPanel.add(scrollPane);

        add(centerPanel, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel();

        

        final JButton okButton = new JButton("Import");
        okButton.addActionListener(new OKButtonListener());
        controlPanel.add(okButton);
        final JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        controlPanel.add(cancelButton);

        add(controlPanel, BorderLayout.SOUTH);

        pack();
        setVisible(true);
    }

    private class OKButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            CollectionNode collectionNode = (CollectionNode)
                    collectionComboBox.getSelectedItem();
            
            RuleSet ruleSet = null;
            
            try {
                ruleSet = (RuleSet)configuration.xStream.fromXML(editorTextArea.getText().trim());
            } catch (XStreamException ex) {
                ExceptionHandler.handle(ex, false, "<p>I could not parse the XML of the rule set you pasted there.</p><p>This could have happened due to multiple causes. First check your XML, ie. check if you selected correctly before copying. If it looks right this error means that some internals have changed and we have no proper backwards compability yet. The below message might help to try and fix your rule set.</p>");
            }

            if (ruleSet == null) {
                return;
            }

            // make sure the name of the import is unique
            // first try given name
            boolean found = false;
            Enumeration siblingRuleSetNodes = collectionNode.children();
            while (siblingRuleSetNodes.hasMoreElements()) {
                RuleSetNode node = (RuleSetNode)siblingRuleSetNodes.nextElement();
                RuleSet rs = (RuleSet)node.getUserObject();
                if (rs.getName().equals(ruleSet.getName())) {
                    found = true;
                    break;
                }
            }
            // then add numbers until we are fine
            if (found == true) {
                String originalName = ruleSet.getName().replaceAll(" *\\(\\d+\\)", "");
                for (int i = 1; found == true; i++) {
                    ruleSet.setName(originalName + " (" + i + ")");
                    found = false;
                    siblingRuleSetNodes = collectionNode.children();
                    while (siblingRuleSetNodes.hasMoreElements()) {
                        RuleSetNode node = (RuleSetNode)siblingRuleSetNodes.nextElement();
                        RuleSet rs = (RuleSet)node.getUserObject();
                        if (rs.getName().equals(ruleSet.getName())) {
                            found = true;
                            break;
                        }
                    }
                }
            }

            RuleSetNode ruleSetNode = new RuleSetNode(ruleSet);
            ((DefaultTreeModel)jRuleSetTree.getModel()).insertNodeInto(
                    ruleSetNode, collectionNode, collectionNode.getChildCount());
            
            dispose();
        }
    }

}
