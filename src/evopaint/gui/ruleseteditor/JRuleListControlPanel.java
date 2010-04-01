/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.gui.ruleseteditor;

import evopaint.pixel.rulebased.Rule;
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
import javax.naming.OperationNotSupportedException;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JPanel;

/**
 *
 * @author tam
 */
public class JRuleListControlPanel extends JPanel {

    public JRuleListControlPanel(final JRuleSetManager jRuleSetManager, final JRuleList jRuleList) {

        JButton btnSave = new JButton("Save");
        btnSave.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });
        add(btnSave);

        JButton btnAdd = new JButton("Add");
        btnAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                IRule newRule = new Rule();
                jRuleList.getRules().add(newRule);
                ((DefaultListModel)jRuleList.getModel()).addElement(newRule);
            }
        });
        add(btnAdd);

        JButton btnEdit = new JButton("Edit");
        btnEdit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (jRuleList.isSelectionEmpty()) {
                    return;
                }
                int index = jRuleList.getSelectedIndex();
                jRuleSetManager.openRuleEditor(jRuleList.getRules().get(index));
            }
        });
        add(btnEdit);

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
                    jRuleList.getRules().add(newRule);
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
        add(btnCopy);

        JButton btnDelete = new JButton("Delete");
        btnDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (jRuleList.isSelectionEmpty()) {
                    return;
                }
                int index = jRuleList.getSelectedIndex();
                DefaultListModel model = ((DefaultListModel)jRuleList.getModel());
                model.remove(index);
                if (index > 0) {
                    jRuleList.setSelectedIndex(index - 1);
                } else if (model.size() > 0) {
                    jRuleList.setSelectedIndex(0);
                }
            }
        });
        add(btnDelete);
    }
}


