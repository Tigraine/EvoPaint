/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.gui.ruleseteditor;

import evopaint.Configuration;
import evopaint.pixel.rulebased.RuleSet;
import evopaint.pixel.rulebased.interfaces.IRule;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;
import javax.swing.JViewport;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

/**
 *
 * @author tam
 */
public class JRuleSetManager extends JPanel {

    private Container contentPane;
    private Configuration configuration;
    private JRuleList jRuleList;
    private JRuleEditor jRuleEditor;


    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public void openRuleEditor(IRule rule) {
        jRuleEditor.setRule(rule);
        ((CardLayout)contentPane.getLayout()).show(contentPane, "editor");
    }

    public JRuleSetManager(RuleSet ruleSet, Configuration configuration, ActionListener OKListener, ActionListener CancelListener) {
        this.configuration = configuration;
        this.contentPane = this;

        setLayout(new CardLayout());

        // FIRST CARD
        // create rule set browser and put it on a scroll pane
        JRuleSetBrowser jRuleSetBrowser = new JRuleSetBrowser();
        JScrollPane scrollPaneForRuleSetBrowser = new JScrollPane(jRuleSetBrowser,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPaneForRuleSetBrowser.setBorder(new LineBorder(Color.GRAY));
        scrollPaneForRuleSetBrowser.setViewportBorder(null);
        //scrollPaneForRuleSetBrowser.setPreferredSize(new Dimension(100, 100));

        // create description of rule set and put it on a scroll pane
        String heading = "<h1 style='text-align: center;'>" + ruleSet.getName() + "</h1>";
        String html = "<html><body>" + heading + "<p>" + ruleSet.getDescription() + "</p></body></html>";
        JTextPane descriptionTextPane = new JTextPane();
        descriptionTextPane.setContentType("text/html");
        descriptionTextPane.setText(html);
        JScrollPane scrollPaneForDescription = new JScrollPane(descriptionTextPane,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPaneForDescription.setBorder(new LineBorder(Color.GRAY));
        scrollPaneForDescription.setViewportBorder(null);
        scrollPaneForDescription.setPreferredSize(new Dimension(300, 100));

        // split browser and description, so we get this:
        // [ browser | description ]
        JSplitPane upperSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                           scrollPaneForRuleSetBrowser, scrollPaneForDescription);
        //upperSplitPane.setDividerLocation(200);
        upperSplitPane.setContinuousLayout(true);
        upperSplitPane.setResizeWeight(0);

        // create rule list in scroll pane
        jRuleList = new JRuleList(this);
        jRuleList.setRules(ruleSet.getRules());
        jRuleList.setBorder(null);
        JScrollPane scrollPaneForRuleList = new JScrollPane(jRuleList,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPaneForRuleList.setBorder(null);
        scrollPaneForRuleList.setViewportBorder(null);

        // create control panel for rules list
        JRuleListControlPanel jRuleListControlPanel = new JRuleListControlPanel(this, jRuleList);

        // and place the rule list and its control panel inside a wrapper panel
        JPanel ruleListPanel = new JPanel();
        ruleListPanel.setLayout(new BoxLayout(ruleListPanel, BoxLayout.Y_AXIS));
        ruleListPanel.setBorder(new LineBorder(Color.GRAY));
        ruleListPanel.add(scrollPaneForRuleList);
        ruleListPanel.add(jRuleListControlPanel);
        
        // fuse upper split pane with rule list panel to this:
        // split [ browser | description ]
        //       [       rule list       ]
        JSplitPane splitPaneVertical = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                           upperSplitPane, ruleListPanel);
        //splitPaneVertical.setDividerLocation(200);
        splitPaneVertical.setContinuousLayout(true);
        splitPaneVertical.setResizeWeight(0.5);

        // set up a control panel for the whole rule set
        JPanel controlPanel = new JPanel();
        JButton btnOK = new JButton("OK");
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
                    jRuleList.getSelectedIndex(),
                    jRuleEditor.getRule());
            ((CardLayout)contentPane.getLayout()).show(contentPane, "manager");
        }

    }

    private class RuleEditorCancelListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            ((CardLayout)contentPane.getLayout()).show(contentPane, "manager");
        }
    }
}