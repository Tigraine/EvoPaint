/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.gui.rulesetmanager;

import evopaint.Configuration;
import evopaint.pixel.rulebased.RuleSet;
import evopaint.pixel.rulebased.RuleSetCollection;
import evopaint.pixel.rulebased.interfaces.IDescribable;
import evopaint.pixel.rulebased.interfaces.IDescribed;
import evopaint.pixel.rulebased.interfaces.INameable;
import evopaint.pixel.rulebased.interfaces.INamed;
import evopaint.util.CollectionNode;
import evopaint.util.FileHandler;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

/**
 *
 * @author tam
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
        JScrollPane viewerScrollPane = new JScrollPane(viewerTextPane,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        viewerScrollPane.setBorder(null);
        viewerScrollPane.setViewportBorder(null);
        //viewerScrollPane.setPreferredSize(new Dimension(300, 100));

        viewerControlPanel = new JPanel();
        viewerControlPanel.setBackground(new Color(0xF2F2F5));
        btnEdit = new JButton("Edit");
        btnEdit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                    ((CardLayout)contentPane.getLayout()).show(contentPane, "editor");
                editorTitleField.setText(title);
                editorDescriptionArea.setText(description);
            }
        });
        viewerControlPanel.add(btnEdit);
        viewerControlPanel.setVisible(false);
        viewer.add(viewerScrollPane, BorderLayout.CENTER);
        viewer.add(viewerControlPanel, BorderLayout.SOUTH);

        // editor
        JPanel editor = new JPanel();
        editor.setLayout(new BorderLayout());
        editor.setBackground(new Color(0xF2F2F5));
        editorTitleField = new JTextField();
        editorDescriptionArea = new JTextArea();
        editorDescriptionArea.setLineWrap(true);
        JPanel editorControlPanel = new JPanel();
        editorControlPanel.setBackground(new Color(0xF2F2F5));
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
        editor.add(editorDescriptionArea, BorderLayout.CENTER);
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
