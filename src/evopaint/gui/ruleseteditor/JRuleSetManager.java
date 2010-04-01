/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.gui.ruleseteditor;

import evopaint.Configuration;
import evopaint.pixel.rulebased.RuleSet;
import evopaint.pixel.rulebased.RuleSetCollection;
import evopaint.pixel.rulebased.interfaces.IRule;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author tam
 */
public class JRuleSetManager extends JPanel {

    private Container contentPane;
    private Configuration configuration;
    private JRuleSetBrowser jRuleSetBrowser;
    private JDescriptionPanel jDescriptionPanel;
    private JRuleList jRuleList;
    private JRuleEditor jRuleEditor;
    JSplitPane splitPaneVertical;


    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public JRuleSetManager(RuleSet ruleSet, Configuration configuration, ActionListener OKListener, ActionListener CancelListener) {
        this.configuration = configuration;
        this.contentPane = this;

        setLayout(new CardLayout());

        // FIRST CARD
        jRuleSetBrowser = new JRuleSetBrowser(new RuleSetBrowserSelectionListener());
        jDescriptionPanel = new JDescriptionPanel(new DescriptionEditorBtnSaveListener());

        // [ browser | description ]
        JSplitPane upperSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                           jRuleSetBrowser, jDescriptionPanel);
        upperSplitPane.setDividerLocation(250);
        upperSplitPane.setContinuousLayout(true);
        upperSplitPane.setResizeWeight(0.1); // most space goes to description

        jRuleList = new JRuleList(new EditRuleBtnListener(), new DoubleClickOnRuleListener());
       
        // [ browser | description ]
        // [       rule list       ]
        splitPaneVertical = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                           upperSplitPane, jRuleList);
        splitPaneVertical.setContinuousLayout(true);
        splitPaneVertical.setResizeWeight(0.2); // most new space goes to rule list
        splitPaneVertical.getBottomComponent().setVisible(false); // hide rule list at first

        // control panel
        JPanel controlPanel = new JPanel();
        JButton btnOK = new JButton("Use");
        btnOK.addActionListener(OKListener);
        controlPanel.add(btnOK);
        JButton btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(CancelListener);
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
        this.jRuleEditor = new JRuleEditor(configuration, new RuleEditorOKListener(), new RuleEditorCancelListener());
        this.jRuleEditor.setVisible(false);
        add(jRuleEditor, "editor");
    }

    private class RuleEditorOKListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            ((DefaultListModel)jRuleList.getModel()).set(
                    jRuleList.getList().getSelectedIndex(),
                    jRuleEditor.getRule());
            ((CardLayout)contentPane.getLayout()).show(contentPane, "manager");
        }

    }

    private class RuleEditorCancelListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            ((CardLayout)contentPane.getLayout()).show(contentPane, "manager");
        }
    }

    private class RuleSetBrowserSelectionListener implements TreeSelectionListener {

        public void valueChanged(TreeSelectionEvent e) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode)
                           jRuleSetBrowser.getTree().getLastSelectedPathComponent();
            
            if (node == null) { // dunno how that could be the case, but what the hell..
                return;
            }

            Object userObject = node.getUserObject();
            if (node.isLeaf()) {
                RuleSet ruleSet = (RuleSet)userObject;
                jRuleList.setRules(ruleSet.getRules());
                jDescriptionPanel.setType(JDescriptionPanel.SET);
                jDescriptionPanel.setBoth(ruleSet.getName(), ruleSet.getDescription());
                splitPaneVertical.getBottomComponent().setVisible(true);
                splitPaneVertical.setDividerLocation(260);
            } else {
                RuleSetCollection ruleSetCollection = (RuleSetCollection)userObject;
                splitPaneVertical.getBottomComponent().setVisible(false);
                jDescriptionPanel.setType(JDescriptionPanel.COLLECTION);
                jDescriptionPanel.setBoth(ruleSetCollection.getName(), ruleSetCollection.getDescription());
            }
            jDescriptionPanel.showEditButton(true);
        }
    }

    private class DescriptionEditorBtnSaveListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    FileHandler fh = FileHandler.getHandler();
                    if (!jDescriptionPanel.getTitle().equals(jDescriptionPanel.getEditedTitle())) {
                        switch (jDescriptionPanel.getType()) {
                            case JDescriptionPanel.SET: fh.updateSetTitle(
                                    jDescriptionPanel.getTitle(), jDescriptionPanel.getEditedTitle());
                            break;
                            case JDescriptionPanel.COLLECTION: fh.updateCollectionTitle(
                                    jDescriptionPanel.getTitle(), jDescriptionPanel.getEditedTitle());
                            break;
                            default: assert(false);
                            return;
                        }
                        jDescriptionPanel.setTitle(jDescriptionPanel.getEditedTitle());
                        jRuleSetBrowser.updateCollections(); // XXX this resets the tree view completely. re-expand the correct node
                    }
                    if (!jDescriptionPanel.getDescription().equals(jDescriptionPanel.getEditedDescription())) {
                        switch (jDescriptionPanel.getType()) {
                            case JDescriptionPanel.SET: fh.updateSetDescription(
                                    jDescriptionPanel.getTitle(), jDescriptionPanel.getEditedDescription());
                            break;
                            case JDescriptionPanel.COLLECTION: fh.updateSetDescription(
                                    jDescriptionPanel.getTitle(), jDescriptionPanel.getEditedDescription());
                            break;
                            default: assert(false);
                            return;
                        }
                        jDescriptionPanel.setDescription(jDescriptionPanel.getEditedDescription());
                    }
                }
            });
        }
    }

    private class EditRuleBtnListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (jRuleList.getList().isSelectionEmpty()) {
                return;
            }
            int index = jRuleList.getList().getSelectedIndex();
            jRuleEditor.setRule((IRule)jRuleList.getModel().get(index));
                ((CardLayout)contentPane.getLayout()).show(contentPane, "editor");
        }
    };

    private class DoubleClickOnRuleListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                int index = jRuleList.getList().locationToIndex(e.getPoint());
                jRuleEditor.setRule((IRule)jRuleList.getModel().get(index));
                ((CardLayout)contentPane.getLayout()).show(contentPane, "editor");
             }
        }
    }
}