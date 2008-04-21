/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package anot;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

/**
 * @author William Lundin Forssén <shazmodan@gmail.com>
 * @author Adam Renberg <tgwizard@gmail.com>
 */
public class ViewTabPanel extends TabPanel {

    protected SimpleDateFormat dateFormat =
            new SimpleDateFormat("yyyy-MM-dd");
    protected SimpleDateFormat timeFormat =
            new SimpleDateFormat("HH:mm");

    public ViewTabPanel() {

        positiveButton.setText("Set");
        positiveButton.setMnemonic('S');
        positiveButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    //TODO: validation
                    String title = titleTextField.getText().trim();
                    String subject = subjectTextField.getText().trim();
                    String description = descriptionTextArea.getText().trim();
                    
                    if (title.isEmpty() ||subject.isEmpty() || description.isEmpty()) {
                        //error
                        JOptionPane.showMessageDialog(null, "Please fill out all fields.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    Date date = dateTimeFormat.parse(dateTextField.getText().trim() + timeTextField.getText().trim());

                    if (Calendar.getInstance().getTime().after(date)) {
                        JOptionPane.showMessageDialog(null, "Activity must be in the future.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    Activity a = getActivityStore().getSelectedActivity();
                    a.setTitle(title);
                    a.setSubject(subject);
                    a.setDescription(description);
                    a.setDate(date);
                    a.setColor(color);

                    getActivityStore().modifiyActivity(a);
                } catch (ParseException ex) {
                    JOptionPane.showMessageDialog(null, "Please format date as (yyyy-mm-dd) and time as (hh:mm).",
                                "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });



        negativeButton.setText("Remove");
        negativeButton.setMnemonic('R');
        negativeButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Activity a = getActivityStore().getSelectedActivity();
                if (a == null) // should never happen
                {
                    return;
                }

                if (JOptionPane.showConfirmDialog(null,
                        "Are you sure you want to remove this activity?",
                        "Confirm removal",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                    getActivityStore().removeActivity(a);
                }
            }
        });

        colorChooserButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Activity a = getActivityStore().getSelectedActivity();
                color = JColorChooser.showDialog(null, "Chose Activity Color",
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
        getActivityStore().addSelectionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Activity a = getActivityStore().getSelectedActivity();
                if (a == null) {
                    return;
                }

                titleTextField.setText(a.getTitle());
                subjectTextField.setText(a.getSubject());
                descriptionTextArea.setText(a.getDescription());

                dateTextField.setText(dateFormat.format(a.getDate()));
                timeTextField.setText(timeFormat.format(a.getDate()));
                
                color = new Color(a.getColor().getRGB());
                colorLabel.repaint();
            }
        });
    //this.activityStore.addListener(this);
    }
}
