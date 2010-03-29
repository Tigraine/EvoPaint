/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.gui.ruleseteditor;

import evopaint.Configuration;
import evopaint.pixel.rulebased.RuleSet;
import java.awt.Dimension;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.border.TitledBorder;

/**
 *
 * @author tam
 */
public class JRuleSetManager extends JFrame {

    protected Configuration configuration;

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public JRuleSetManager(RuleSet ruleSet, Configuration configuration) {
        this.configuration = configuration;

        JRuleSetBrowser jRuleSetBrowser = new JRuleSetBrowser();
        JRuleSetControlPanel jRuleSetControlPanel = new JRuleSetControlPanel();
        
        JScrollPane scrollPaneForRuleSetBrowser = new JScrollPane(jRuleSetBrowser,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPaneForRuleSetBrowser.setBorder(new TitledBorder("Rule Sets"));
        scrollPaneForRuleSetBrowser.setBackground(getBackground());

        JPanel leftSide = new JPanel();
        leftSide.setLayout(new BoxLayout(leftSide, BoxLayout.Y_AXIS));
        leftSide.add(scrollPaneForRuleSetBrowser);
        leftSide.add(jRuleSetControlPanel);
        
        final JRuleSetEditor jRuleSetEditor = new JRuleSetEditor(ruleSet);

        //Create a split pane with the two scroll panes in it.
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                           leftSide, jRuleSetEditor);
        //splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(150);
        scrollPaneForRuleSetBrowser.setMinimumSize(new Dimension(100, 0));

        add(splitPane);

        setPreferredSize(new Dimension(1100,600));
    }
}
