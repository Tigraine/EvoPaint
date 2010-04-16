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

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import evopaint.Configuration;
import evopaint.gui.rulesetmanager.util.NamedObjectListCellRenderer;
import evopaint.pixel.rulebased.RuleSet;
import evopaint.pixel.rulebased.RuleSetCollection;
import evopaint.pixel.rulebased.interfaces.IRule;
import evopaint.util.CollectionNode;
import evopaint.util.ExceptionHandler;
import evopaint.util.RuleSetNode;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.FlavorEvent;
import java.awt.datatransfer.FlavorListener;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Enumeration;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

/**
 *
 * @author Markus Echterhoff <tam@edu.uni-klu.ac.at>
 */
public class JRuleSetManager extends JPanel implements TreeSelectionListener, FlavorListener{

    private Configuration configuration;
    private RuleSet selectedRuleSet;
    private JRuleSetTree jRuleSetTree;
    private Container contentPane;
    private JRuleSetBrowser jRuleSetBrowser;
    private JDescriptionPanel jDescriptionPanel;
    private JRuleList jRuleList;
    private JRuleEditorPanel jRuleEditor;
    private JSplitPane splitPaneVertical;
    JButton btnUse;

    public RuleSet getSelectedRuleSet() {
        // return selectedRuleSet.getCopy(); // a copy, or we will change painted rule sets
        // it turns out that this is actually what the user expects
        return selectedRuleSet;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public JRuleSetManager(Configuration configuration, ActionListener okListener, ActionListener cancelListener) {
        this.configuration = configuration;
        this.contentPane = this;

        setLayout(new CardLayout());

        Toolkit.getDefaultToolkit().getSystemClipboard().addFlavorListener(this);

        DefaultTreeModel treeModel = configuration.fileHandler.readCollections();
        jRuleSetTree = new JRuleSetTree(treeModel, new TreeDoubleClickListener());
        jRuleSetTree.addTreeSelectionListener(this);

        jRuleList = new JRuleList(configuration, jRuleSetTree, new EditRuleBtnListener(), new DoubleClickOnRuleListener());

        // FIRST CARD
        jRuleSetBrowser = new JRuleSetBrowser(configuration, jRuleSetTree, jRuleList);
        jDescriptionPanel = new JDescriptionPanel(configuration, jRuleSetTree);

        // [ browser | description ]
        JSplitPane upperSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                           jRuleSetBrowser, jDescriptionPanel);
        upperSplitPane.setDividerLocation(250);
        upperSplitPane.setContinuousLayout(true);
        upperSplitPane.setResizeWeight(0.1); // most space goes to description

        // [ browser | description ]
        // [       rule list       ]
        splitPaneVertical = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                           upperSplitPane, jRuleList);
        splitPaneVertical.setContinuousLayout(true);
        splitPaneVertical.setResizeWeight(0.2); // most new space goes to rule list
        splitPaneVertical.getBottomComponent().setVisible(false); // hide rule list at first

        // control panel
        JPanel controlPanel = new JPanel();
        btnUse = new JButton("Use");
        btnUse.addActionListener(okListener);
        btnUse.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (jRuleList.isDirty()) {
                    jRuleList.clean();
                }
            }
        });
        btnUse.setEnabled(false);
        controlPanel.add(btnUse);
        JButton btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (jRuleList.isDirty()) {
                    jRuleList.clean();
                }
            }
        });
        btnCancel.addActionListener(cancelListener);
        controlPanel.add(btnCancel);

        // create a main panel to contain everything, this will be added
        // to the card layout of this panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(20, 20));
        mainPanel.setBorder(new LineBorder(getBackground(), splitPaneVertical.getDividerSize()));
        mainPanel.add(splitPaneVertical, BorderLayout.CENTER);
        mainPanel.add(controlPanel, BorderLayout.SOUTH);
        add(mainPanel, "manager");

        // SECOND CARD
        // editor
        // added by edit button listener or double click listener
    }

    public void valueChanged(TreeSelectionEvent e) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)e.getPath().getLastPathComponent();
        Object userObject = node.getUserObject();

        if (userObject == null) {
            selectedRuleSet = null;
            btnUse.setEnabled(false);
            return;
        }

        if (userObject instanceof RuleSetCollection) {
            splitPaneVertical.getBottomComponent().setVisible(false);
            selectedRuleSet = null;
            btnUse.setEnabled(false);
            return;
        }

        if (userObject instanceof RuleSet) {
            splitPaneVertical.getBottomComponent().setVisible(true);
            splitPaneVertical.setDividerLocation(260);
            selectedRuleSet = (RuleSet)userObject;
            btnUse.setEnabled(true);
            return;
        }

        assert(false);
    }



    public void flavorsChanged(FlavorEvent e) {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemSelection();
        Transferable contentObject = clipboard.getContents(null);

        if (contentObject == null ||
                false == contentObject.isDataFlavorSupported(DataFlavor.stringFlavor)) {
            return;
        }

        try {
            final String contentString =
                    (String) contentObject.getTransferData(DataFlavor.stringFlavor);

            if (contentString == null) {
                return;
            }

            if (false == contentString.startsWith("<evopaint.pixel.rulebased.RuleSet>")) {
                return;
            }

            final JDialog importDialog = new JDialog(
                    (JFrame)SwingUtilities.getWindowAncestor(contentPane),
                    "Import Rule Set", true);
            importDialog.setLayout(new BoxLayout(importDialog.getContentPane(), BoxLayout.Y_AXIS));

            importDialog.add(Box.createVerticalStrut(10));
            JPanel labelAlignmentPanel = new JPanel();
            labelAlignmentPanel.add(new JLabel("Import to Collection:"));
            importDialog.add(labelAlignmentPanel);
            importDialog.add(Box.createVerticalStrut(10));

            // collections combo box
            JPanel comboBoxAlignmentPanel = new JPanel();
            DefaultMutableTreeNode root = (DefaultMutableTreeNode)
                    jRuleSetTree.getModel().getRoot();
            Enumeration children = root.children();
            if (children == null) {
                System.out.println("no collections to import into");
                return;
            }
            DefaultComboBoxModel comboBoxModel = new DefaultComboBoxModel();
            while (children.hasMoreElements()) {
                CollectionNode collectionNode =
                        (CollectionNode)children.nextElement();
                comboBoxModel.addElement(collectionNode);
            }
            final JComboBox collectionComboBox = new JComboBox(comboBoxModel);
            collectionComboBox.setRenderer(new NamedObjectListCellRenderer());
            comboBoxAlignmentPanel.add(collectionComboBox);
            importDialog.add(comboBoxAlignmentPanel);

            // control panel
            JPanel controlPanel = new JPanel();
            JButton btnOK = new JButton("OK");
            btnOK.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    CollectionNode collectionNode = (CollectionNode)
                            collectionComboBox.getSelectedItem();
                    XStream xStream = new XStream(new DomDriver());
                    RuleSet ruleSet = (RuleSet)xStream.fromXML(contentString);

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
                    importDialog.dispose();
                }
            });
            importDialog.add(Box.createVerticalStrut(10));
            controlPanel.add(btnOK);

            JButton btnCancel = new JButton("Cancel");
            btnCancel.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    importDialog.dispose();
                }
            });
            controlPanel.add(btnCancel);

            importDialog.add(controlPanel);

            importDialog.pack();
            importDialog.setVisible(true);
        } catch (UnsupportedFlavorException ex) {
            ExceptionHandler.handle(ex);
        } catch (IOException ex) {
            ExceptionHandler.handle(ex);
        }
    }

    private class RuleEditorOKListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            IRule rule = jRuleEditor.createRule();
            String errorMsg = rule.validate();
            if (errorMsg == null) {
                jRuleList.replaceSelectedRule(rule);
                ((CardLayout)contentPane.getLayout()).show(contentPane, "manager");
            }
            else {
                JOptionPane.showMessageDialog(contentPane, errorMsg, "Fail!", JOptionPane.ERROR_MESSAGE);
            }
        }

    }

    private class RuleEditorCancelListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            ((CardLayout)contentPane.getLayout()).show(contentPane, "manager");
        }
    }
    
    private class EditRuleBtnListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            IRule selectedRule = jRuleList.getSelectedRule();

            if (selectedRule == null) {
                return;
            }

            if (jRuleEditor != null) {
                remove(jRuleEditor);
            }
            jRuleEditor = new JRuleEditorPanel(configuration, selectedRule, new RuleEditorOKListener(), new RuleEditorCancelListener());
            add(jRuleEditor, "editor");
            ((CardLayout)contentPane.getLayout()).show(contentPane, "editor");
        }
    };

    private class DoubleClickOnRuleListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                int index = jRuleList.locationToIndex(e.getPoint());

                if (index == -1) {
                    return;
                }
                
                IRule selectedRule = jRuleList.getSelectedRule();

                assert (selectedRule != null);

                if (jRuleEditor != null) {
                    remove(jRuleEditor);
                }
                jRuleEditor = new JRuleEditorPanel(configuration, selectedRule, new RuleEditorOKListener(), new RuleEditorCancelListener());
                add(jRuleEditor, "editor");
                ((CardLayout)contentPane.getLayout()).show(contentPane, "editor");
             }
        }
    }

    private class TreeDoubleClickListener extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {
            TreePath path = jRuleSetTree.getPathForLocation(e.getX(), e.getY());
            if (e.getClickCount() == 2) {

                if (path == null) { // this is a very rare event, but it does occur for whatever reason
                    return;
                }
                
                DefaultMutableTreeNode node =
                        (DefaultMutableTreeNode)path.getLastPathComponent();

                if (node instanceof RuleSetNode) {
                    RuleSet ruleSet = (RuleSet)node.getUserObject();
                    selectedRuleSet = ruleSet;
                    btnUse.doClick();
                }
            }
        }
    }
}