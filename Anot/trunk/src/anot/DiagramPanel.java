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
public class DiagramPanel extends JPanel implements ActionListener,
        MouseListener, MouseMotionListener {

    ActivityStore activityStore;
    //...where instance variables are declared:
    JPopupMenu popup;
    JCheckBoxMenuItem reverseSortMenuItem;
    JRadioButtonMenuItem sortByDateMenuItem;
    JRadioButtonMenuItem sortBySubjectMenuItem;
    
    int x = -1;
    int y = -1;

    public DiagramPanel() {
        //TODO: background color?

        addMouseListener(this);
        addMouseMotionListener(this);

        popup = new JPopupMenu();
        ButtonGroup group = new ButtonGroup();


        sortByDateMenuItem = new JRadioButtonMenuItem("Sort by date");
        sortByDateMenuItem.setSelected(true);
        group.add(sortByDateMenuItem);
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

        sortBySubjectMenuItem = new JRadioButtonMenuItem("Sort by subject");
        group.add(sortBySubjectMenuItem);
        sortBySubjectMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

            }
        });
        popup.add(sortBySubjectMenuItem);

        popup.add(new JPopupMenu.Separator());

        reverseSortMenuItem = new JCheckBoxMenuItem("Reverse sort");
        reverseSortMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (activityStore.isReverseSort()) {
                    activityStore.setReverseSort(false);
                } else {
                    activityStore.setReverseSort(true);
                }
            }
        });
        popup.add(reverseSortMenuItem);

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
        
        Color color = Color.white;
        int increment = getHeight()/30;
        for (int i = 0; i < getHeight(); i += increment) {
            g.setColor(color);
            g.fillRect(0,i, getWidth(), increment);
            color = new Color(color.getRed()-3, color.getGreen()-3, color.getBlue()-2);
        }

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
            
            g.setColor(Color.gray);
            for (double y = 0; y <= h; y += pixelsPerDay) {
                g.drawLine(p*40+10, (int)Math.floor(getHeight()-y), p*40+9+35, (int)Math.floor(getHeight()-y));
            }
            
            
            p++;
        }
        
        g.setColor(Color.orange);
        g.drawLine(x, 0, x, getHeight());
        g.drawLine(0, y, getWidth(), y);
    }

    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    public void mouseClicked(MouseEvent e) {
        x = e.getX();
        repaint();
    }

    public void mousePressed(MouseEvent e) {
        
    }

    public void mouseReleased(MouseEvent e) {
        
    }

    public void mouseEntered(MouseEvent e) {
        
    }

    public void mouseExited(MouseEvent e) {
        
    }

    public void mouseDragged(MouseEvent e) {
        
    }

    public void mouseMoved(MouseEvent e) {
        y = e.getY();
        x = e.getX();
        repaint();
    }
}
