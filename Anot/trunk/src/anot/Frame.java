/*
 */
package anot;

import java.util.*;

/**
 * 
 * @author Adam Renberg <tgwizard@gmail.com>
 */
public class Frame {

    public static void main(String[] args) {
        System.out.println ("SVN-test"); // testing again 2
        ActivityStore as = ActivityStoreBuilder.loadActivityStoreFromJar("activities.xml");
        
        Iterator<Activity> i = as.iterator();
        
        while (i.hasNext()) {
            System.out.println (i.next());
        }
        
        ActivityStoreBuilder.saveActivityStoreToFile(as, "data/written.xml");
        
        ActivityStore as2 = ActivityStoreBuilder.loadActivityStoreFromFile("data/written.xml");
        
        Iterator<Activity> i2 = as2.iterator();
        
        while (i2.hasNext()) {
            System.out.println (i2.next());
        }
    }
}
