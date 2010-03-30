/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.gui.ruleseteditor;

import evopaint.pixel.rulebased.Rule;
import evopaint.pixel.rulebased.RuleSet;
import evopaint.pixel.rulebased.interfaces.IRule;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

/**
 *
 * @author tam
 */
public class JRuleSetEditor extends JPanel {
    JPanel wrapListControlDescription;
    JRuleList jRuleList;
    JRuleEditor jRuleEditor;
    JEditorPane descriptionEditorPane;

    public JRuleSetEditor(final RuleSet ruleSet) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        wrapListControlDescription = new JPanel();
        wrapListControlDescription.setLayout(new BoxLayout(wrapListControlDescription, BoxLayout.Y_AXIS));
        add(wrapListControlDescription);
        
        // rule list
        jRuleList = new JRuleList(ruleSet.getRules());
        final JScrollPane scrollPaneForRuleList = new JScrollPane(jRuleList,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPaneForRuleList.setBorder(new TitledBorder("Rules"));
        scrollPaneForRuleList.setBackground(getBackground());
        wrapListControlDescription.add(scrollPaneForRuleList);


        // control panel
        final JPanel controlPanel = new JPanel();
        JButton btnAdd = new JButton("Add");
        btnAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                IRule newRule = new Rule();
                ruleSet.getRules().add(newRule);
                ((DefaultListModel)jRuleList.getModel()).addElement(newRule);
            }
        });
        controlPanel.add(btnAdd);

        JButton btnEdit = new JButton("Edit");
        btnEdit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (jRuleList.isSelectionEmpty()) {
                    return;
                }
                int index = jRuleList.getSelectedIndex();
                jRuleEditor.setRule(ruleSet.getRules().get(index));
                wrapListControlDescription.setVisible(false);
                jRuleEditor.setVisible(true);
            }
        });
        controlPanel.add(btnEdit);

        JButton btnDelete = new JButton("Delete");
        btnDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (jRuleList.isSelectionEmpty()) {
                    return;
                }
                int index = jRuleList.getSelectedIndex();
                ((DefaultListModel)jRuleList.getModel()).remove(index);
            }
        });
        controlPanel.add(btnDelete);

        JButton btnCopy = new JButton("Copy");
        btnCopy.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (jRuleList.isSelectionEmpty()) {
                    return;
                }
                int index = jRuleList.getSelectedIndex();
                final IRule protoRule = (IRule)((DefaultListModel)jRuleList.getModel()).get(index);
                try {
                    IRule newRule;
                    ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
                    ObjectOutputStream out = new ObjectOutputStream(outByteStream);
                    out.writeObject(protoRule);
                    ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(outByteStream.toByteArray()));
                    newRule = (IRule) in.readObject();
                    ruleSet.getRules().add(newRule);
                    ((DefaultListModel)jRuleList.getModel()).addElement(newRule);
                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                    System.exit(1);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    System.exit(1);
                }
            }
        });
        controlPanel.add(btnCopy);
        
        wrapListControlDescription.add(controlPanel);

        String heading = "<br><br><h1 style='text-align: center;'>" + ruleSet.getName() + "</h1>";
        String html = "<html>" + heading + "<p style='width: 500px;'>" + ruleSet.getDescription() + "</p></html>";
        descriptionEditorPane = new JEditorPane("text/html", html);
        descriptionEditorPane.setEditable(false);
        wrapListControlDescription.add(descriptionEditorPane);

        jRuleEditor = new JRuleEditor();
        jRuleEditor.setVisible(false);
        add(jRuleEditor);

        // open rule editor on double click
        jRuleList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int index = jRuleList.locationToIndex(e.getPoint());
                    jRuleEditor.setRule(ruleSet.getRules().get(index));

                    wrapListControlDescription.setVisible(false);
                    jRuleEditor.setVisible(true);
                 }
            }
        });

        jRuleEditor.addComponentListener(new ComponentListener() {

            public void componentResized(ComponentEvent e) {}

            public void componentMoved(ComponentEvent e) {}

            public void componentShown(ComponentEvent e) {}

            public void componentHidden(ComponentEvent e) {
                if (jRuleEditor.isDirty()) {
                    ruleSet.getRules().set(jRuleList.getSelectedIndex(), jRuleEditor.getRule());
                    //ruleSet.s
                    // replace old rule with new one in list
                    ((DefaultListModel)jRuleList.getModel()).set(
                            jRuleList.getSelectedIndex(),
                            jRuleEditor.getRule());
                    jRuleList.revalidate();
                }
            }
        });
    }
}


