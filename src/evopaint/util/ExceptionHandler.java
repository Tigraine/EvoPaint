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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;

/**
 *
 * @author Markus Echterhoff <tam@edu.uni-klu.ac.at>
 */
public class ExceptionHandler {
    private static JDialog dialog;
    private static JTextArea textArea;

    public ExceptionHandler(JFrame mainFrame) {
        dialog = new JDialog(mainFrame, "FUUUUUUUUUUUUUUUUUUUUUUUUUU", true);
        dialog.setLayout(new BorderLayout());

        JLabel errorLabel = new JLabel("EvoPaint caught an exception. This is never good so we shut down for now.");
        dialog.add(errorLabel, BorderLayout.NORTH);

        textArea = new JTextArea();
        dialog.add(textArea, BorderLayout.CENTER);
        
        final JButton okButton = new JButton("This is my fault");
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(1);
            }
        });
        dialog.add(okButton, BorderLayout.SOUTH);
    }

    public static void handle(Exception ex) {
        if (dialog != null) {
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter, true);
            ex.printStackTrace(printWriter);
            printWriter.flush();
            stringWriter.flush();
            textArea.setText(stringWriter.toString());
            dialog.pack();
            dialog.setVisible(true);
        } else {
            ex.printStackTrace();
            System.exit(1);
        }
    }
}
