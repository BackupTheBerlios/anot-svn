/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package anot;

import java.awt.event.*;
import java.text.SimpleDateFormat;
import javax.swing.*;

/**
 * 
 * @author Adam Renberg <tgwizard@gmail.com>
 */
public class ViewTabPanel extends TabPanel {

    protected SimpleDateFormat dateFormat = 
            new SimpleDateFormat("yyyy-MM-dd");
    protected SimpleDateFormat timeFormat = 
            new SimpleDateFormat("HH:mm:ss");
    
    public ViewTabPanel() {

        positiveButton.setText("Set");
        positiveButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

            }
        });



        negativeButton.setText("Remove");
        negativeButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Activity a = getActivityStore().getSelectedActivity();
                if (a == null) // should never happen
                    return;
                
                if (JOptionPane.showConfirmDialog(null,
                        "Are you sure you want to remove this activity?",
                        "Confirm removal",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE)
                        == JOptionPane.YES_OPTION) {
                    getActivityStore().removeActivity(a);
                }
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
                if (a == null)
                    return;
                
                titleTextField.setText(a.getTitle());
                subjectTextField.setText(a.getSubject());
                descriptionTextArea.setText(a.getDescription());
                
                dateTextField.setText(dateFormat.format(a.getDate()));
                timeTextField.setText(timeFormat.format(a.getDate()));
                
            }
            
        });
    //this.activityStore.addListener(this);
    }
}
