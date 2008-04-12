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
        ActivityStore as = ActivityStoreBuilder.loadActivityStoreFromJar("activities.xml");
        
        System.out.println(as);
        
        ActivityStoreBuilder.saveActivityStoreToFile(as, "data/written.xml");
        
        System.out.println("---");
        
        ActivityStore as2 = ActivityStoreBuilder.loadActivityStoreFromFile("data/written.xml");
        
        System.out.println(as);
    }
}
