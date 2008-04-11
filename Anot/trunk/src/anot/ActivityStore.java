/*
 */
package anot;

import java.util.*;

/**
 * 
 * @author Adam Renberg <tgwizard@gmail.com>
 */
public class ActivityStore {

    LinkedList<Activity> activities;
    
    public ActivityStore() {
    }
    
    public ActivityStore(String filename) {
        
    }
    
    public void sortActivites() {
        
    }
    
    public void addActivity(Activity a) {
        activities.addLast(a);
    }
    
    public void saveToXml() {
        
    }
    
    public static ActivityStore loadFromXml(String filename) {
        return null;
    }
}
