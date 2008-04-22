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

        popup = null;

        //Add listener to components that can bring up popup menus.
        addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                maybeShowPopup(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                maybeShowPopup(e);
            }

            private void maybeShowPopup(MouseEvent e) {
                if (e.isPopupTrigger() && popup != null) {
                    // the switch of invokers is to make the menu work both
                    // in the frame and as a popup here
                    Component invoker = popup.getInvoker();
                    popup.show(e.getComponent(),
                            e.getX(), e.getY());
                    popup.setInvoker(invoker);
                }
            }
        });
        
        // this is soooo cool
        new javax.swing.Timer(3600 * 1000, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                repaint();
            }
        }).start();
    }

    public void setPopupMenu(JPopupMenu popup) {
        this.popup = popup;
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

        if (activityStore.getSize() == 0) {
            g.setColor(Color.gray);
            String s = "Please add activities from below.";
            int w = g.getFontMetrics(font).stringWidth(s);
            g.drawString(s, getWidth() / 2 - w / 2, getHeight() / 2);
            return;
        }

        long nowTime = Calendar.getInstance().getTime().getTime();

        double nDays = (activityStore.getLatestDate().getTime() - nowTime) / (double) (1000 * 60 * 60 * 24);

        double pixelsPerDay = ((getHeight() - 14) / nDays);

        g.setColor(Color.gray);
        g.drawLine(10, 10, 10, getHeight() - 10);

        /*if ((int) nDays % 3 == 0) {
        System.out.println("llama");
        drawGrade(g, "" + (int) (nDays - nDays / 3), (int) (nDays - nDays / 3), pixelsPerDay);
        drawGrade(g, "" + (int) (nDays / 2), (int) (nDays / 2), pixelsPerDay);
        drawGrade(g, "" + (int) (nDays / 3), (int) (nDays / 3), pixelsPerDay);
        } else*/
        drawGrade(g, "" + (int) (nDays), (int) (nDays), pixelsPerDay);
        if (nDays > 3) {
            drawGrade(g, "" + (int) (nDays - nDays / 4), (int) (nDays - nDays / 4), pixelsPerDay);
            drawGrade(g, "" + (int) (nDays / 2), (int) (nDays / 2), pixelsPerDay);
            drawGrade(g, "" + (int) (nDays / 4), (int) (nDays / 4), pixelsPerDay);
        } else {
            for (int i = 0; i < nDays; i++) {
                drawGrade(g, "" + i, i, pixelsPerDay);
            }
        }

        Iterator<Activity> i = activityStore.iterator();
        int p = 0;

        while (i.hasNext()) {
            Activity a = i.next();

            double days = (a.getDate().getTime() - nowTime);
            days /= (1000 * 60 * 60 * 24);

            int h = (int) (days * pixelsPerDay);

            //System.out.println(p + " :::: " + h);

            //g.fillRect(p * 40 + 10, getHeight() - 10 - (int) h * 10, 35, (int) h * 10);
            if (activityStore.getSelectedActivity() == a) {
                Color ac = a.getColor();
                Color c = new Color(Math.min(255, ac.getRed() + 40),
                        Math.min(255, ac.getGreen() + 40),
                        Math.min(255, ac.getBlue() + 40));
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

            double remainingDays = (a.getDate().getTime() - nowTime) / (double) (1000 * 60 * 60 * 24);
            String s;
            if (remainingDays < 1.0) {
                s = Integer.toString((int) (remainingDays * 24)) + "h";
            } else {
                s = Integer.toString((int) remainingDays) + "d";
            }
            int w = g.getFontMetrics(font).stringWidth(s);
            g.drawString(s, p * stapleNewPos + stapleStart + stapleWidth / 2 - w / 2, getHeight() - h - 3);

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
