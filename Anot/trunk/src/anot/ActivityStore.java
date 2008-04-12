/*
 */
package anot;

import java.awt.event.*;
import java.util.*;

/**
 * 
 * @author Adam Renberg <tgwizard@gmail.com>
 */
public class ActivityStore {

    LinkedList<Activity> activities;
    LinkedList<ActionListener> listeners;
    Comparator<Activity> comparator;

    public ActivityStore() {
        activities = new LinkedList<Activity>();
        listeners = new LinkedList<ActionListener>();
        comparator = null;
    }

    protected void notifyListeners() {
        ActionEvent ae = new ActionEvent(this, -1, "Activities Modified");
        for (ActionListener al : listeners) {
            al.actionPerformed(ae);
        }
    }

    public void sortActivites(Comparator<Activity> comparator) {
        this.comparator = comparator;
        if (comparator != null) {
            Collections.sort(activities, comparator);
        }
        notifyListeners();
    }

    public void addActivity(Activity a) {
        activities.addLast(a);
        if (comparator != null) {
            Collections.sort(activities, comparator);
        }
        notifyListeners();
    }

    public Iterator<Activity> iterator() {
        return activities.iterator();
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Activity a : activities) {
            sb.append(a);
        }
        return sb.toString();
    }
}
