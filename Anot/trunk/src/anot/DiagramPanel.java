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
    //...where instance variables are declared:
    JPopupMenu popup;
    JMenuItem reverseSortMenuItem;
    JMenuItem sortByDateMenuItem;
    JMenuItem sortBySubjectMenuItem;

    public DiagramPanel() {
        //TODO: background color?



        popup = new JPopupMenu();

        sortByDateMenuItem = new JMenuItem("Sort by date");
        sortByDateMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                activityStore.sortActivites(new Comparator<Activity>() {

                    public int compare(Activity o1, Activity o2) {
                        return o1.getDate().compareTo(o2.getDate());
                    }
                });
            }
        });
        popup.add(sortByDateMenuItem);

        sortBySubjectMenuItem = new JMenuItem("Sort by subject");
        popup.add(sortBySubjectMenuItem);

        popup.add(new JPopupMenu.Separator());

        reverseSortMenuItem = new JMenuItem("Reverse sort");
        popup.add(reverseSortMenuItem);
        //menuItem.addActionListener(this);

        //Add listener to components that can bring up popup menus.
        addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent e) {
                maybeShowPopup(e);
            }

            public void mouseReleased(MouseEvent e) {
                maybeShowPopup(e);
            }

            private void maybeShowPopup(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    popup.show(e.getComponent(),
                            e.getX(), e.getY());
                }
            }
        });
    }

    public void setActivityStore(ActivityStore activityStore) {
        if (this.activityStore != null) {
            this.activityStore.removeListener(this);
        }
        this.activityStore = activityStore;
        this.activityStore.addListener(this);

        repaint();
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(Color.white);
        g.fillRect(0, 0, getWidth(), getHeight());

        Iterator<Activity> i = activityStore.iterator();
        int p = 0;

        long nowTime = Calendar.getInstance().getTime().getTime();

        double daysHeight = (activityStore.getLatestDate().getTime() - nowTime) / (1000 * 60 * 60 * 24);

        double pixelsPerDay = ((getHeight()) / daysHeight);

        while (i.hasNext()) {
            Activity a = i.next();

            double days = (a.getDate().getTime() - nowTime);
            days /= (1000 * 60 * 60 * 24);

            int h = (int) (days * pixelsPerDay);

            System.out.println(p + " :::: " + h);

            //g.fillRect(p * 40 + 10, getHeight() - 10 - (int) h * 10, 35, (int) h * 10);
            g.setColor(Color.pink);
            g.fillRect(p * 40 + 10, getHeight() - h, 35, h);
            p++;
        }
    }

    public void actionPerformed(ActionEvent e) {
        repaint();
    }
}
