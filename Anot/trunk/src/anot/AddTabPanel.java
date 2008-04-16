/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package anot;

import java.awt.event.*;
import java.text.ParseException;
import java.util.*;

/**
 * 
 * @author Adam Renberg <tgwizard@gmail.com>
 */
public class AddTabPanel extends TabPanel {

    public AddTabPanel() {

        positiveButton.setText("Add");
        positiveButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    //TODO: validation
                    String title = titleTextField.getText().trim();
                    String subject = subjectTextField.getText().trim();
                    String description = descriptionTextArea.getText().trim();

                    System.out.println(title + " : " + subject + " : " + description);

                    Date date = dateFormat.parse(dateTextField.getText().trim() +
                            timeTextField.getText().trim());
                    
                    if (Calendar.getInstance().getTime().after(date)) {
                        //TODO: popup
                        return;
                    }
                    
                    Activity a = new Activity(title);
                    a.setSubject(subject);
                    a.setDescription(description);
                    a.setDate(date);
                    
                    activityStore.addActivity(a);
                    
                } catch (ParseException ex) {
                    //TODO: popup
                }
            }
        });



        negativeButton.setText("Clear");
        negativeButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                titleTextField.setText("");
                subjectTextField.setText("");
                descriptionTextArea.setText("");
                dateTextField.setText("yyyy-mm-dd");
                timeTextField.setText("hh:mm:ss");
                
            }
        });
    }

    public void actionPerformed(ActionEvent e) {
        
    }
}
