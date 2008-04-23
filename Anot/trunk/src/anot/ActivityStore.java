/*
 */
package anot;

import java.awt.Color;
import java.awt.event.*;
import java.util.*;

/**
 * @author William Lundin Forssén <shazmodan@gmail.com>
 * @author Adam Renberg <tgwizard@gmail.com>
 */
public class ActivityStore {

    protected static SortComparatorFactory scfactory = new SortComparatorFactory();

    public static SortComparatorFactory getSortComparatorFactory() {
        return scfactory;
    }
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
        sortComparator = scfactory.createSortComparator("date");
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

    public static abstract class SortComparator implements Comparator<Activity> {

        public abstract String getType();

        @Override
        public abstract SortComparator clone();
    }

    public static class SortComparatorFactory {

        LinkedList<SortComparator> scs;

        public SortComparatorFactory() {
            scs = new LinkedList<SortComparator>();
            scs.add(new DateSortComparator());
            scs.add(new SubjectSortComparator());
            scs.add(new ColorSortComparator());
        }

        public void addSortComparator(SortComparator sc) {
            scs.add(sc);
        }

        public SortComparator createSortComparator(String type) {
            for (SortComparator sc : scs) {
                if (sc.getType().equals(type)) {
                    return sc.clone();
                }
            }
            System.err.println("No such SortComparator: " + type);
            return scs.getFirst();
        }
    }

    private static class SubjectSortComparator extends SortComparator {

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

        @Override
        public SortComparator clone() {
            return new SubjectSortComparator();
        }
    }

    private static class DateSortComparator extends SortComparator {

        public int compare(Activity o1, Activity o2) {
            return o1.getDate().compareTo(o2.getDate());
        }

        @Override
        public String getType() {
            return "date";
        }

        @Override
        public SortComparator clone() {
            return new DateSortComparator();
        }
    }

    private static class ColorSortComparator extends SortComparator {

        public int compare(Activity o1, Activity o2) {
            Color c1 = o1.getColor();
            float[] hsb1 = Color.RGBtoHSB(c1.getRed(), c1.getGreen(), c1.getBlue(), null);
            Color c2 = o2.getColor();
            float[] hsb2 = Color.RGBtoHSB(c2.getRed(), c2.getGreen(), c2.getBlue(), null);
            // for hue
            if (hsb1[0] == hsb2[0]) {
                // for saturation
                if (hsb1[1] == hsb2[1]) {
                    // for brightness
                    if (hsb1[2] == hsb2[2]) {
                        return 0;
                    } else if (hsb1[2] < hsb2[2]) {
                        return -1;
                    } else {
                        return 1;
                    } // end brightness
                } else if (hsb1[1] < hsb2[1]) {
                    return -1;
                } else {
                    return 1;
                } // end saturation
            } else if (hsb1[0] < hsb2[0]) {
                return -1;
            } else {
                return 1;
            } // end hue
        }

        @Override
        public String getType() {
            return "color";
        }

        @Override
        public SortComparator clone() {
            return new ColorSortComparator();
        }
    }
}
