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

package evopaint.util;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;

/**
 *
 * @author Markus Echterhoff <tam@edu.uni-klu.ac.at>
 */
public class ExceptionHandler {
    private static ExceptionHandler instance;

    private static String fatalHeading = "Fatal Error";
    private static String fatalMessage = "<p>EvoPaint has seriously fucked up and is going to shut down now.</p><p>If you want to help prevent this error from happening again, take a moment to write down what you did just before now and send it together with the text below to Markus Echterhoff using tam@edu.uni-klu.ac.at.</p>";
    private static String defaultHeading = "That didn't work";
    private static String defaultMessage = "<p>EvoPaint has encountered a problem doing stuff.</p><p>If you want to help prevent this error from happening again, take a moment to write down what you did just before now and send it together with the text below to the EvoPaint developers.</p>";

    private boolean fatal;
    private JDialog dialog;
    private JTextPane messagePane;
    private JTextArea exceptionTextArea;

    public ExceptionHandler() {
    }

    private ExceptionHandler(JFrame mainFrame) {
        dialog = new JDialog(mainFrame, "Sorry...", true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setPreferredSize(new Dimension(800, 600));

        messagePane = new JTextPane();
        messagePane.setContentType("text/html");
        messagePane.setEditable(false);
        messagePane.setBorder(new LineBorder(new JPanel().getBackground(), 10));
        dialog.add(messagePane, BorderLayout.NORTH);

        exceptionTextArea = new JTextArea();
        exceptionTextArea.setBorder(null);
        JScrollPane scrollPane = new JScrollPane(exceptionTextArea,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(new LineBorder(new JPanel().getBackground(), 10));
        scrollPane.setViewportBorder(new BevelBorder(BevelBorder.LOWERED));
        
        dialog.add(scrollPane, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel();
        final JButton okButton = new JButton("Damn");
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
                if (fatal) {
                    System.exit(1);
                }
            }
        });
        controlPanel.add(okButton);
        final JButton okButton2 = new JButton("Crap");
        okButton2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
                if (fatal) {
                    System.exit(1);
                }
            }
        });
        controlPanel.add(okButton2);
        
        dialog.add(controlPanel, BorderLayout.SOUTH);
    }

    public static void init(JFrame mainFrame) {
        instance = new ExceptionHandler(mainFrame);
    }

    // do not change the signature of this method. needed by awt
    public void handle(Throwable t) {
        handle(t, true);
    }

    public static void handle(Throwable t, boolean fatal) {
        handle(t, fatal, fatal ? fatalMessage : defaultMessage);
    }

    public static void handle(Throwable t, boolean fatal, String msg) {
        if (instance == null) {
            t.printStackTrace();
        }

        instance.handleInternal(t, fatal, msg);
    }

    private void handleInternal(Throwable t, boolean fatal, String msg) {
        if (instance == null) {
            t.printStackTrace();
            System.exit(1);
        }

        this.fatal = fatal;

        messagePane.setText("<html><body style='padding:10; background: ffb1ba;'><h1 style='text-align: center;'>" + (fatal ? fatalHeading : defaultHeading) + "</h1>" + msg + "</body></html>");

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter, true);
        t.printStackTrace(printWriter);
        printWriter.flush();
        stringWriter.flush();
        exceptionTextArea.setText(stringWriter.toString());

        dialog.pack();
        dialog.setVisible(true);
    }
}
