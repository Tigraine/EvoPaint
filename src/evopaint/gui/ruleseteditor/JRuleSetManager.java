/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.gui.ruleseteditor;

import evopaint.Configuration;
import evopaint.pixel.rulebased.RuleSet;
import evopaint.pixel.rulebased.interfaces.IRule;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;
import javax.swing.border.TitledBorder;

/**
 *
 * @author tam
 */
public class JRuleSetManager extends JPanel {

    private Container contentPane;
    private Configuration configuration;
    private JPanel managingPanel;
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

        this.managingPanel = new JPanel();
        //managingPanel.setBackground(Color.red);
        //managingPanel.setLayout(new GridBagLayout());
        
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.fill = GridBagConstraints.BOTH;

        // create rule set browser and put it on a scroll pane
        JRuleSetBrowser jRuleSetBrowser = new JRuleSetBrowser();
        JScrollPane scrollPaneForRuleSetBrowser = new JScrollPane(jRuleSetBrowser,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPaneForRuleSetBrowser.setBorder(new TitledBorder("Rule Sets"));
        scrollPaneForRuleSetBrowser.setPreferredSize(new Dimension(200, 200));
        scrollPaneForRuleSetBrowser.setBackground(getBackground());

        // create description of rule set
        String heading = "<h1 style='text-align: center;'>" + ruleSet.getName() + "</h1>";
        String html = "<html><body>" + heading + "<p>" + ruleSet.getDescription() + "</p></body></html>";
        JTextPane descriptionTextPane = new JTextPane();
        descriptionTextPane.setContentType("text/html");
        descriptionTextPane.setText(html);
        
        //descriptionEditorPane.setEditable(false);
        JScrollPane scrollPaneForDescription = new JScrollPane(descriptionTextPane,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        //scrollPaneForDescription.setBorder(new TitledBorder("Description"));
        //scrollPaneForDescription.setBackground(getBackground());
        scrollPaneForDescription.setViewportBorder(null);
        //scrollPaneForDescription.setMinimumSize(new Dimension(300, 300));
        scrollPaneForDescription.setPreferredSize(new Dimension(300, 100));

        // split browser and description
        JSplitPane splitPaneHorizontal = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                           scrollPaneForRuleSetBrowser, scrollPaneForDescription);
        splitPaneHorizontal.setDividerLocation(200);
        splitPaneHorizontal.setContinuousLayout(true);
        splitPaneHorizontal.setResizeWeight(0);
        //scrollPaneForRuleSetBrowser.setMinimumSize(new Dimension(100, 0));

        // create rule list in scroll pane
        jRuleList = new JRuleList(this);
        jRuleList.setRules(ruleSet.getRules());
        JScrollPane scrollPaneForRuleList = new JScrollPane(jRuleList,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPaneForRuleList.setBackground(getBackground());

        JPanel ruleListPanel = new JPanel();
        ruleListPanel.setLayout(new BoxLayout(ruleListPanel, BoxLayout.Y_AXIS));
        ruleListPanel.setBorder(new TitledBorder("Rules"));
        ruleListPanel.add(scrollPaneForRuleList);
        ruleListPanel.add(new JRuleListControlPanel(this, jRuleList));
        //ruleListPanel.setPreferredSize(new Dimension(400, 200));

        // split [ browser | description ]
        //       [       rule list       ]
        JSplitPane splitPaneVertical = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                           splitPaneHorizontal, ruleListPanel);
        splitPaneVertical.setDividerLocation(200);
        splitPaneVertical.setContinuousLayout(true);
        splitPaneVertical.setResizeWeight(0.5);
        
        this.managingPanel.add(splitPaneVertical, constraints);

        JPanel controlPanel = new JPanel();
        JButton btnOK = new JButton("OK");
        btnOK.addActionListener(OKListener);
        controlPanel.add(btnOK);

        JButton btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(CancelListener);
        controlPanel.add(btnCancel);
        constraints.gridy = 1;
        this.managingPanel.add(controlPanel, constraints);
        managingPanel.setPreferredSize(new Dimension(500, 500));

        JScrollPane fuckingScrollPane = new JScrollPane(managingPanel,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        fuckingScrollPane.add(this.managingPanel);
        fuckingScrollPane.setPreferredSize(new Dimension(500, 500));
        add(managingPanel, "manager");

        //JRuleListControlPanel jRuleSetControlPanel = new JRuleListControlPanel(this, jRuleList);
        //add(jRuleSetControlPanel);

        // editor
        this.jRuleEditor = new JRuleEditor(new RuleEditorOKListener(), new RuleEditorCancelListener());
        this.jRuleEditor.setVisible(false);
        add(jRuleEditor, "editor");

        //setPreferredSize(mainFrame.getSize());
        //pack();
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