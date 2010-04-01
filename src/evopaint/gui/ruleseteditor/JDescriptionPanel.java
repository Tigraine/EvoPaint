/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.gui.ruleseteditor;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;

/**
 *
 * @author tam
 */
public class JDescriptionPanel extends JPanel {
    public static final int SET = 0;
    public static final int COLLECTION = 1;

    private int type;
    private String title; // cos getName() was declared in super class
    private String description;
    private JPanel contentPane;
    private JTextPane viewerTextPane;
    private JPanel viewerControlPanel;
    private JTextField editorTitleField;
    private JTextArea editorDescriptionArea;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public void showEditButton(boolean show) {
        viewerControlPanel.setVisible(show);
    }

    private void render() {
        if (title != null) {
            String heading = "<h1 style='text-align: center;'>" + title + "</h1>";
            String html = "<html><body>" + heading + "<p>" + description + "</p></body></html>";
            viewerTextPane.setText(html);
            return;
        }
        String heading = "<h1 style='text-align: center;'>DON'T PANIC</h1>";
        String html = "<html><body>" + heading + "</body></html>";
        viewerTextPane.setText(html);
    }

    public JDescriptionPanel(ActionListener descriptionEditorBtnSaveListener) {
        this.contentPane = this;

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
        JButton btnEdit = new JButton("Edit");
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
        btnSave.addActionListener(descriptionEditorBtnSaveListener);
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
        
        render();
    }
}
