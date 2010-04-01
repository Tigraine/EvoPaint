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
import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
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

    public void openRuleEditor(IRule rule) {
        
    }

    public JRuleSetManager(RuleSet ruleSet, Configuration configuration, ActionListener OKListener, ActionListener CancelListener) {
        this.configuration = configuration;
        this.contentPane = this;

        setLayout(new CardLayout());

        // FIRST CARD
        // create rule set browser and put it on a scroll pane
        jRuleSetBrowser = new JRuleSetBrowser(new RuleSetBrowserSelectionListener());
        JScrollPane scrollPaneForRuleSetBrowser = new JScrollPane(jRuleSetBrowser,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPaneForRuleSetBrowser.setBorder(null);
        scrollPaneForRuleSetBrowser.setViewportBorder(null);

        // create buttons for control panel of browser
        JPanel browserControlPanel = new JPanel();
        browserControlPanel.setBackground(Color.WHITE);
        JButton browserBtnAdd = new JButton("Add");
        browserBtnAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });
        browserControlPanel.add(browserBtnAdd);
        JButton browserBtnCopy = new JButton("Copy");
        browserBtnCopy.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });
        browserControlPanel.add(browserBtnCopy);
        JButton browserBtnDelete = new JButton("Delete");
        browserBtnDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });
        browserControlPanel.add(browserBtnDelete);

        // fuse browser scrollpane and controls onto a shared panel
        JPanel browserPanel = new JPanel();
        browserPanel.setLayout(new BorderLayout());
        browserPanel.setBackground(Color.WHITE);
        browserPanel.setBorder(new LineBorder(Color.GRAY));
        browserPanel.add(scrollPaneForRuleSetBrowser, BorderLayout.CENTER);
        browserPanel.add(browserControlPanel, BorderLayout.SOUTH);

        // create description panel
        jDescriptionPanel = new JDescriptionPanel();

        // split browser and description, so we get this:
        // [ browser | description ]
        JSplitPane upperSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                           browserPanel, jDescriptionPanel);
        upperSplitPane.setDividerLocation(250);
        upperSplitPane.setContinuousLayout(true);
        upperSplitPane.setResizeWeight(0);

        // create rule list in scroll pane
        jRuleList = new JRuleList(new EditRuleBtnListener(), new DoubleClickOnRuleListener());
       
        // fuse upper split pane with rule list panel to this:
        // split [ browser | description ]
        //       [       rule list       ]
        splitPaneVertical = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                           upperSplitPane, jRuleList);
        //splitPaneVertical.setDividerLocation(200);
        splitPaneVertical.setContinuousLayout(true);
        splitPaneVertical.setResizeWeight(0.2); // most new space goes to rule list
        splitPaneVertical.getBottomComponent().setVisible(false);

        // set up a control panel for the whole rule set
        JPanel controlPanel = new JPanel();
        JButton btnOK = new JButton("Use");
        btnOK.addActionListener(OKListener);
        controlPanel.add(btnOK);
        JButton btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(CancelListener);
        controlPanel.add(btnCancel);

        // create a main panel to contain everything, this will be added
        // to the card layout of the frame
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(20, 20));
        mainPanel.setBorder(new LineBorder(getBackground(), splitPaneVertical.getDividerSize()));
        mainPanel.add(splitPaneVertical, BorderLayout.CENTER);
        mainPanel.add(controlPanel, BorderLayout.SOUTH);
        add(mainPanel, "manager");

        // SECOND CARD
        // editor
        this.jRuleEditor = new JRuleEditor(new RuleEditorOKListener(), new RuleEditorCancelListener());
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
                           jRuleSetBrowser.getLastSelectedPathComponent();
            
            if (node == null) { // dunno how that could be the case, but what the hell..
                return;
            }

            Object userObject = node.getUserObject();
            if (node.isLeaf()) {
                RuleSet ruleSet = (RuleSet)userObject;
                jRuleList.setRules(ruleSet.getRules());
                jDescriptionPanel.setBoth(ruleSet.getName(), ruleSet.getDescription());
                splitPaneVertical.getBottomComponent().setVisible(true);
                splitPaneVertical.setDividerLocation(300);
            } else {
                RuleSetCollection ruleSetCollection = (RuleSetCollection)userObject;
                splitPaneVertical.getBottomComponent().setVisible(false);
                jDescriptionPanel.setBoth(ruleSetCollection.getName(), ruleSetCollection.getDescription());
            }
            jDescriptionPanel.showEditButton(true);
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