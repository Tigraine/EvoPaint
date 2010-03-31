/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package evopaint.gui.ruleseteditor;

import javax.swing.JTextPane;

/**
 *
 * @author tam
 */
public class JRuleSetDescriptionPane extends JTextPane {
    private String myName; // cos getName() was declared in super class
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMyName() {
        return myName;
    }

    public void setMyName(String myName) {
        this.myName = myName;
        render();
    }

    public void setBoth(String myName, String description) {
        this.myName = myName;
        this.description = description;
        render();
    }

    public void clear() {
        myName = null;
        description = null;
        render();
    }

    private void render() {
        if (myName != null) {
            String heading = "<h1 style='text-align: center;'>" + myName + "</h1>";
            String html = "<html><body>" + heading + "<p>" + description + "</p></body></html>";
            setText(html);
            return;
        }
        String heading = "<h1 style='text-align: center;'>DON'T PANIC</h1>";
        String html = "<html><body>" + heading + "</body></html>";
        setText(html);
    }

    public JRuleSetDescriptionPane() {
        setContentType("text/html");
        setEditable(false);
        render();
    }
}
