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
public class ViewTabPanel extends TabPanel {

    public ViewTabPanel() {

        positiveButton.setText("Set");
        positiveButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

            }
        });



        negativeButton.setText("Remove");
        negativeButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

            }
        });
    }
}
