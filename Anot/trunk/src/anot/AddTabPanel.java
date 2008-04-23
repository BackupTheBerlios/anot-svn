/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package anot;

import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;
import java.util.*;
import javax.swing.JColorChooser;
import javax.swing.JOptionPane;

/**
 * @author William Lundin Forssén <shazmodan@gmail.com>
 * @author Adam Renberg <tgwizard@gmail.com>
 */
public class AddTabPanel extends TabPanel {

    public AddTabPanel() {

        positiveButton.setText("Add");
        positiveButton.setMnemonic('A');
        positiveButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    //TODO: validation of date & time
                    String title = titleTextField.getText().trim();
                    String subject = subjectTextField.getText().trim();
                    String description = descriptionTextArea.getText().trim();

                    //if title or subject or description = empty
                    if (title.isEmpty() || subject.isEmpty() || description.isEmpty()) {
                        //error
                        JOptionPane.showMessageDialog(getRootPane(), "Please fill out all fields.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    System.out.println(title + " : " + subject + " : " + description);

                    Date date = dateTimeFormat.parse(dateTextField.getText().trim() +
                            timeTextField.getText().trim());

                    if (Calendar.getInstance().getTime().after(date)) {
                        JOptionPane.showMessageDialog(getRootPane(), "Activity must be in the future.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    Activity a = new Activity(title);
                    a.setSubject(subject);
                    a.setDescription(description);
                    a.setDate(date);
                    a.setColor(color);

                    getActivityStore().addActivity(a);
                    getActivityStore().setSelectedActivity(a);

                } catch (ParseException ex) {
                    JOptionPane.showMessageDialog(getRootPane(), "Please format date as (yyyy-mm-dd) and time as (hh:mm).",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });



        negativeButton.setText("Clear");
        negativeButton.setMnemonic('C');
        negativeButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                titleTextField.setText("");
                subjectTextField.setText("");
                descriptionTextArea.setText("");
                dateTextField.setText("yyyy-mm-dd");
                timeTextField.setText("hh:mm");

            }
        });

        colorChooserButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Activity a = getActivityStore().getSelectedActivity();
                color = JColorChooser.showDialog(getRootPane(), "Chose Activity Color",
                        color);
                colorLabel.repaint();
            }
        });
    }

    @Override
    public void setActivityStore(ActivityStore activityStore) {
        //if (this.activityStore != null)
        //this.activityStore.removeListener(this);
        doSetActivityStore(activityStore);
    //this.activityStore.addListener(this);
    }
}
