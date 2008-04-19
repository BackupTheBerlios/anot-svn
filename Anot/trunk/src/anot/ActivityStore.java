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
    Activity selectedActivity;
    LinkedList<ActionListener> listeners;
    LinkedList<ActionListener> selectionListeners;
    Comparator<Activity> comparator;
    boolean reverseSort;

    public ActivityStore() {
        activities = new LinkedList<Activity>();
        selectedActivity = null;
        listeners = new LinkedList<ActionListener>();
        selectionListeners = new LinkedList<ActionListener>();
        comparator = new Comparator<Activity>() {

            public int compare(Activity o1, Activity o2) {
                return o1.getDate().compareTo(o2.getDate());
            }
        };
    }

    protected void notifyListeners() {
        ActionEvent ae = new ActionEvent(this, -1, "Activities Modified");
        for (ActionListener al : listeners) {
            al.actionPerformed(ae);
        }
    }

    public void addListener(ActionListener al) {
        listeners.add(al);
    }

    public void removeListener(ActionListener al) {
        listeners.remove(al);
    }

    protected void notifySelectionListeners() {
        ActionEvent ae = new ActionEvent(this, -1, "Activity Selected");
        for (ActionListener al : selectionListeners) {
            al.actionPerformed(ae);
        }
    }

    public void addSelectionListener(ActionListener al) {
        selectionListeners.add(al);
    }

    public void removeSelectionListener(ActionListener al) {
        selectionListeners.remove(al);
    }

    public Activity getSelectedActivity() {
        return selectedActivity;
    }

    public void setSelectedActivity(Activity selectedActivity) {
        this.selectedActivity = selectedActivity;
        notifySelectionListeners();
    }

    public void sortActivites(Comparator<Activity> comparator) {
        this.comparator = comparator;
        sortActivities();
    }

    public boolean isReverseSort() {
        return reverseSort;
    }

    public void setReverseSort(boolean reverseSort) {

        this.reverseSort = reverseSort;
        sortActivities();
    }

    public void addActivity(Activity a) {
        activities.addLast(a);
        sortActivities();
    }
    
    public void removeActivity(Activity a) {
        if (a == selectedActivity) {
            setSelectedActivity(null);
        }
        activities.remove(a);
        notifyListeners();
    }
    
    public void modifiyActivity(Activity a) {
        sortActivities();
        if (a == selectedActivity) {
            setSelectedActivity(a);
        }
    }

    protected void sortActivities() {
        if (comparator != null) {
            Collections.sort(activities, comparator);
        }
        if (reverseSort) {
            Collections.reverse(activities);
        }
        notifyListeners();
    }

    public Iterator<Activity> iterator() {
        return activities.iterator();
    }

    public Date getLatestDate() {
        Date date = Calendar.getInstance().getTime();
        for (Activity a : activities) {
            if (a.getDate().after(date)) {
                date = a.getDate();
            }
        }
        return date;
    }

    public int getSize() {
        return activities.size();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Activity a : activities) {
            sb.append(a);
            sb.append('\n');
        }
        sb.setLength(sb.length() - 1);
        return sb.toString();
    }
}
