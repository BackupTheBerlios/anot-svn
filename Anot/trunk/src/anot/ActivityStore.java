/*
 */
package anot;

import java.awt.event.*;
import java.util.*;

/**
 * @author William Lundin Forssén <shazmodan@gmail.com>
 * @author Adam Renberg <tgwizard@gmail.com>
 */
public class ActivityStore {

    String filename;
    LinkedList<Activity> activities;
    Activity selectedActivity;
    LinkedList<ActionListener> listeners;
    LinkedList<ActionListener> selectionListeners;
    SortComparator sortComparator;
    boolean reverseSort;

    public ActivityStore(String filename) {
        this.filename = filename;
        activities = new LinkedList<Activity>();
        selectedActivity = null;
        listeners = new LinkedList<ActionListener>();
        selectionListeners = new LinkedList<ActionListener>();
        sortComparator = createDateComparator();
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

    public SortComparator getSortComparator() {
        return sortComparator;
    }

    public void sortActivites(SortComparator comparator) {
        this.sortComparator = comparator;
        sortActivities();
        save();
    }

    public boolean isReverseSort() {
        return reverseSort;
    }

    public void setReverseSort(boolean reverseSort) {
        this.reverseSort = reverseSort;
        sortActivities();
        save();
    }

    public void addActivity(Activity a) {
        activities.addLast(a);
        sortActivities();
        save();
    }

    public void removeActivity(Activity a) {
        if (a == selectedActivity) {
            setSelectedActivity(null);
        }
        activities.remove(a);
        notifyListeners();
        save();
    }

    public void modifiyActivity(Activity a) {
        sortActivities();
        if (a == selectedActivity) {
            setSelectedActivity(a);
        }
        save();
    }

    protected void sortActivities() {
        if (sortComparator != null) {
            Collections.sort(activities, sortComparator);
        }
        if (reverseSort) {
            Collections.reverse(activities);
        }
        notifyListeners();
        save();
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

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }

    public void save() {
        if (!filename.isEmpty()) {
            ActivityStoreBuilder.saveActivityStoreToFile(this, filename);
        }
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

    public static SortComparator createDateComparator() {
        return new SortComparator() {

            public int compare(Activity o1, Activity o2) {
                return o1.getDate().compareTo(o2.getDate());
            }

            @Override
            public String getType() {
                return "date";
            }
        };
    }

    public static SortComparator createSubjectComparator() {
        return new SortComparator() {

            public int compare(Activity o1, Activity o2) {
                int ret = o1.getSubject().compareTo(o2.getSubject());
                if (ret == 0) {
                    return o1.getDate().compareTo(o2.getDate());
                }
                return ret;
            }

            @Override
            public String getType() {
                return "subject";
            }
        };
    }
    
    public static abstract class SortComparator implements Comparator<Activity> {
        public abstract String getType();
    }
}
