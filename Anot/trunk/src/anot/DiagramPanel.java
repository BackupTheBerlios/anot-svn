/*
 *
 */
package anot;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import java.util.*;

/**
 * 
 * @author Adam Renberg <tgwizard@gmail.com>
 * @author William Lundin Forssén <shazmodan@gmail.com>
 */
public class DiagramPanel extends JPanel implements MouseListener,
        MouseMotionListener {

    ActivityStore activityStore;
    //...where instance variables are declared:
    JPopupMenu popup;
    JCheckBoxMenuItem reverseSortMenuItem;
    JRadioButtonMenuItem sortByDateMenuItem;
    JRadioButtonMenuItem sortBySubjectMenuItem;
    int x = -1;
    int y = -1;
    private int stapleWidth = 35;
    private int stapleStart = 40;
    private int stapleNewPos = 40;
    Font font;

    public DiagramPanel() {
        //TODO: background color?

        addMouseListener(this);
        addMouseMotionListener(this);

        font = new Font("Verdana", Font.BOLD, 10);

        popup = new JPopupMenu();
        ButtonGroup group = new ButtonGroup();


        sortByDateMenuItem = new JRadioButtonMenuItem("Sort by date");
        
        group.add(sortByDateMenuItem);
        sortByDateMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                activityStore.sortActivites(ActivityStore.createDateComparator());
            }
        });
        popup.add(sortByDateMenuItem);

        sortBySubjectMenuItem = new JRadioButtonMenuItem("Sort by subject");
        
        group.add(sortBySubjectMenuItem);
        sortBySubjectMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                activityStore.sortActivites(ActivityStore.createSubjectComparator());
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

    protected ActivityStore getActivityStore() {
        return activityStore;
    }

    protected void checkForScrollbar() {
        setPreferredSize(new Dimension(activityStore.getSize() * stapleNewPos + 2 * stapleStart, 0));
        revalidate();
    }

    public void setActivityStore(ActivityStore activityStore) {
        //FIXME: make the following work:
        /*if (this.activityStore != null) {
        this.activityStore.removeListener(this);
        }*/
        
        sortByDateMenuItem.setSelected(ActivityStore.createDateComparator().getType().equals(
                activityStore.getSortComparator().getType()));
        
        sortBySubjectMenuItem.setSelected(ActivityStore.createSubjectComparator().getType().equals(
                activityStore.getSortComparator().getType()));
        
        reverseSortMenuItem.setState(activityStore.isReverseSort());
        
        this.activityStore = activityStore;
        this.activityStore.addListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                checkForScrollbar();
                revalidate();
                repaint();
            }
        });

        this.activityStore.addSelectionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                repaint();
            }
        });

        repaint();
    }

    @Override
    public void paint(Graphics g) {
        if (activityStore == null) {
            return;
        }

        g.setFont(font);

        g.setColor(Color.white);
        g.fillRect(0, 0, getWidth(), getHeight());

        Color color = Color.white;
        /*int increment = getHeight() / 30;
        for (int i = 0; i < getHeight(); i += increment) {
        g.setColor(color);
        g.fillRect(0, i, getWidth(), increment);
        color = new Color(color.getRed() - 3, color.getGreen() - 3, color.getBlue() - 2);
        }*/

        Iterator<Activity> i = activityStore.iterator();
        int p = 0;

        long nowTime = Calendar.getInstance().getTime().getTime();

        double nDays = (activityStore.getLatestDate().getTime() - nowTime) / (double)(1000 * 60 * 60 * 24);

        double pixelsPerDay = ((getHeight()) / nDays);

        g.setColor(Color.gray);
        g.drawLine(10, 10, 10, getHeight() - 10);

        /*if ((int) nDays % 3 == 0) {
            System.out.println("llama");
            drawGrade(g, "" + (int) (nDays - nDays / 3), (int) (nDays - nDays / 3), pixelsPerDay);
            drawGrade(g, "" + (int) (nDays / 2), (int) (nDays / 2), pixelsPerDay);
            drawGrade(g, "" + (int) (nDays / 3), (int) (nDays / 3), pixelsPerDay);
        } else*/ 
        if (nDays < 13) {
            drawGrade(g, "" + (int) (nDays - nDays / 3), (int) (nDays - nDays / 3), pixelsPerDay);
            drawGrade(g, "" + (int) (nDays / 3), (int) (nDays / 3), pixelsPerDay);
        } else {
            drawGrade(g, "" + (int) (nDays - nDays / 4), (int) (nDays - nDays / 4), pixelsPerDay);
            drawGrade(g, "" + (int) (nDays / 2), (int) (nDays / 2), pixelsPerDay);
            drawGrade(g, "" + (int) (nDays / 4), (int) (nDays / 4), pixelsPerDay);
        }
        
        System.out.println(nDays);

        while (i.hasNext()) {
            Activity a = i.next();

            double days = (a.getDate().getTime() - nowTime);
            days /= (1000 * 60 * 60 * 24);

            int h = (int) (days * pixelsPerDay);

            //System.out.println(p + " :::: " + h);

            //g.fillRect(p * 40 + 10, getHeight() - 10 - (int) h * 10, 35, (int) h * 10);
            if (activityStore.getSelectedActivity() == a) {
                Color ac = a.getColor();
                Color c = new Color (Math.min(255, ac.getRed()+40),
                        Math.min(255, ac.getGreen()+40),
                        Math.min(255, ac.getBlue()+40));
                g.setColor(c);
            } else {
                g.setColor(a.getColor());
            }
            g.fillRect(p * stapleNewPos + stapleStart, getHeight() - h - 1, stapleWidth, h);

            g.setColor(Color.gray);
            for (double line = 0; line <= h; line += pixelsPerDay) {
                g.drawLine(p * stapleNewPos + stapleStart,
                        (int) Math.floor(getHeight() - line),
                        p * stapleNewPos + stapleStart - 1 + stapleWidth,
                        (int) Math.floor(getHeight() - line));
            }


            p++;
        }

    //g.setColor(Color.orange);
    //g.drawLine(x, 0, x, getHeight());
    //g.drawLine(0, y, getWidth(), y);
    }

    public void drawGrade(Graphics g, String s, int days, double pixelsPerDay) {
        g.drawString(s, 12,
                (int) Math.floor(getHeight() - pixelsPerDay * days) - 2);
        g.drawLine(10, (int) Math.floor(getHeight() - pixelsPerDay * days),
                30, (int) Math.floor(getHeight() - pixelsPerDay * days));
    }

    public void mouseClicked(MouseEvent e) {
        x = e.getX();

        //FIXME: this is not beautiful
        for (int p = 0; p < activityStore.getSize(); p++) {
            int a = p * stapleNewPos + stapleStart;
            if (x >= a && x <= (a + stapleWidth)) {
                Iterator<Activity> iter = activityStore.iterator();
                while (p > 0) {
                    iter.next();
                    p--;
                }
                activityStore.setSelectedActivity(iter.next());
                return;
            }
        }
        activityStore.setSelectedActivity(null);
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
    //y = e.getY();
    //x = e.getX();
    //repaint();
    }
}
