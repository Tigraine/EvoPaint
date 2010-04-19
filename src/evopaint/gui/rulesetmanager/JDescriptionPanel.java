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

import evopaint.Configuration;
import evopaint.pixel.rulebased.interfaces.IDescribable;
import evopaint.pixel.rulebased.interfaces.IDescribed;
import evopaint.pixel.rulebased.interfaces.INameable;
import evopaint.pixel.rulebased.interfaces.INamed;
import evopaint.util.ExceptionHandler;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

/**
 *
 * @author Markus Echterhoff <tam@edu.uni-klu.ac.at>
 */
public class JDescriptionPanel extends JPanel implements TreeSelectionListener {

    private String title; // cos getName() was declared in super class
    private String description;
    private Configuration configuration;
    private JRuleSetTree tree;
    private JPanel contentPane;
    private JTextPane viewerTextPane;
    private JPanel viewerControlPanel;
    private JTextField editorTitleField;
    private JTextArea editorDescriptionArea;
    private JButton btnEdit;

    private String defaultTitle = "DON'T PANIC";
    private String defaultDescription = "Things to remember:<ul>" +
            "<li>Every pixel follows a set of rules. <b>Only the first rule</b> whose conditions are met is ever executed (beginning from the top of the list). If none of the rules matches, your pixel will idle.</li><br>" +
            "<li>You can <b>reorder</b> the list of rules of any rule set using drag and drop.</li><br>" +
            "<li>To <b>rename</b> or <b>describe</b> a rule set, click the 'Edit' button of the description panel.</li></ul>";

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        render();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        render();
    }

    public void setBoth(String title, String description) {
        this.title = title;
        this.description = description;
        render();
    }

    public String getEditedTitle() {
        return editorTitleField.getText();
    }

    public String getEditedDescription() {
        return editorDescriptionArea.getText();
    }

    public void clear() {
        title = null;
        description = null;
        render();
    }

    private void render() {
        String heading = "<h1 style='text-align: center;'>" + title + "</h1>";
        String html = "<html><body>" + heading + "<p>" + newLineToBreak(description) + "</p></body></html>";
        viewerTextPane.setText(html);
    }

    private String newLineToBreak(String s) {
        return s.replaceAll("\n", "<br>");
    }

    public void valueChanged(TreeSelectionEvent e) {
        ((CardLayout)contentPane.getLayout()).show(contentPane, "viewer");
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)e.getPath().getLastPathComponent();
        Object userObject = node.getUserObject();
        if (userObject == null) {
            title = defaultTitle;
            description = defaultDescription;
            render();
            viewerControlPanel.setVisible(false);
            return;
        }
        title = ((INamed)userObject).getName();
        description = ((IDescribed)userObject).getDescription();
        render();
        viewerControlPanel.setVisible(true);
        viewerTextPane.setCaretPosition(0);
    }

    public JDescriptionPanel(Configuration configuration, JRuleSetTree tree) {
        this.configuration = configuration;
        this.tree = tree;
        this.contentPane = this;
        tree.addTreeSelectionListener(this);

        setBorder(new LineBorder(Color.GRAY));
        setLayout(new CardLayout());

        // viewer
        JPanel viewer = new JPanel();
        viewer.setLayout(new BorderLayout());

        viewerTextPane = new JTextPane();
        viewerTextPane.setContentType("text/html");
        viewerTextPane.setEditable(false);
        viewerTextPane.setBackground(Color.WHITE);
        viewerTextPane.addHyperlinkListener(new HyperlinkListener() {

            public void hyperlinkUpdate(HyperlinkEvent e) {
                if (e.getEventType() != HyperlinkEvent.EventType.ACTIVATED) {
                    return;
                }
                try {
                    Desktop.getDesktop().browse(e.getURL().toURI());
                } catch (URISyntaxException ex) {
                    ExceptionHandler.handle(ex, false, "The URL \"" + e.getURL().toString() + "\" is invalid.");
                } catch (IOException ex) {
                    ExceptionHandler.handle(ex, false, "Could not open the URI for some reason");
                }
            }
        });
        JScrollPane viewerScrollPane = new JScrollPane(viewerTextPane,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        viewerScrollPane.setBorder(null);
        viewerScrollPane.setViewportBorder(null);

        viewerControlPanel = new JPanel();
        viewerControlPanel.setBackground(new Color(0xF2F2F5));
        btnEdit = new JButton(new ImageIcon(getClass().getResource("icons/button-edit.png")));
        btnEdit.setToolTipText("Edit name and description of this collection or rule set");
        btnEdit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                    ((CardLayout)contentPane.getLayout()).show(contentPane, "editor");
                editorTitleField.setText(title);
                editorDescriptionArea.setText(description);
                editorDescriptionArea.setCaretPosition(0);
            }
        });
        viewerControlPanel.add(btnEdit);
        viewerControlPanel.setVisible(false);
        viewer.add(viewerScrollPane, BorderLayout.CENTER);
        viewer.add(viewerControlPanel, BorderLayout.SOUTH);

        // editor
        JPanel editor = new JPanel();
        editor.setBorder(new LineBorder(getBackground(), 10));
        editor.setLayout(new BorderLayout(10, 10));
        //editor.setBackground(new Color(0xF2F2F5));
        editorTitleField = new JTextField();
        editorTitleField.setBorder(new BevelBorder(BevelBorder.LOWERED));
        editorDescriptionArea = new JTextArea();
        editorDescriptionArea.setLineWrap(true);
        editorDescriptionArea.setWrapStyleWord(true);
        JScrollPane editorScrollPane = new JScrollPane(editorDescriptionArea,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        editorScrollPane.setBorder(null);
        editorScrollPane.setViewportBorder(new BevelBorder(BevelBorder.LOWERED));

        JPanel editorControlPanel = new JPanel();
        //editorControlPanel.setBackground(new Color(0xF2F2F5));
        JButton btnSave = new JButton("Save");
        btnSave.addActionListener(new DescriptionEditorBtnSaveListener());
        btnSave.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               ((CardLayout)contentPane.getLayout()).show(contentPane, "viewer");
            }
        });
        editorControlPanel.add(btnSave);
        JButton btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ((CardLayout)contentPane.getLayout()).show(contentPane, "viewer");
            }
        });
        editorControlPanel.add(btnCancel);
        editor.add(editorTitleField, BorderLayout.NORTH);
        editor.add(editorScrollPane, BorderLayout.CENTER);
        editor.add(editorControlPanel, BorderLayout.SOUTH);

        add(viewer, "viewer");
        add(editor, "editor");

        title = defaultTitle;
        description = defaultDescription;
        render();
    }

    private class DescriptionEditorBtnSaveListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            DefaultMutableTreeNode selectedNode =
                (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
            DefaultMutableTreeNode parentNode =
                (DefaultMutableTreeNode) selectedNode.getParent();
            
            String desiredName = editorTitleField.getText();
            if (false == tree.isUniqueSiblingName(parentNode,
                    selectedNode.getUserObject(), desiredName)) {
                return;
            }

            // edit name and description in tree
            Object userObject = selectedNode.getUserObject();
            String oldName = ((INamed)userObject).getName();
            ((INameable)userObject).setName(desiredName);
            ((IDescribable)userObject).setDescription(editorDescriptionArea.getText());
            // fire node change event
            DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
            model.nodesChanged(parentNode,
                    new int [] {parentNode.getIndex(selectedNode)});

            // update our own display
            title = editorTitleField.getText();
            description = editorDescriptionArea.getText();
            render();
        }

    }
}
