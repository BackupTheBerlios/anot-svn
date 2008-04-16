/*
 */
package anot;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import java.util.*;

/**
 * 
 * @author Adam Renberg <tgwizard@gmail.com>
 */
public class DiagramPanel extends JPanel implements ActionListener {

    ActivityStore activityStore;
    
    public DiagramPanel() {
        //TODO: background color?
    }

    public void setActivityStore(ActivityStore activityStore) {
        if (this.activityStore != null)
            this.activityStore.removeListener(this);
        this.activityStore = activityStore;
        this.activityStore.addListener(this);
        
        repaint();
    }
    
    @Override
    public void paint(Graphics g) {
        g.setColor(Color.white);
        g.fillRect(0, 0, getWidth(), getHeight());
        
        g.setColor(Color.pink);
        Iterator<Activity> i = activityStore.iterator();
        int p = 0;
        
        Date now = Calendar.getInstance().getTime();
        long nowTime = now.getTime();
        System.out.println(now);
        
        while (i.hasNext()) {
            Activity a = i.next();
            
            long h = a.getDate().getTime() - nowTime;
            h /= (1000*60*60*24);
            
            g.fillRect(p*40+10, getHeight()-10-(int)h*10, 35, (int)h*10);
            
            p++;
        }
    }

    public void actionPerformed(ActionEvent e) {
        repaint();
    }
}
