/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package anot;

import java.awt.event.*;

/**
 * 
 * @author Adam Renberg <tgwizard@gmail.com>
 */
public class AddTabPanel extends TabPanel {

    public AddTabPanel() {

        positiveButton.setText("Add");
        positiveButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

            }
        });



        negativeButton.setText("Clear");
        negativeButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

            }
        });
    }
}
