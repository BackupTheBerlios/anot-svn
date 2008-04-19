/*
 */
package anot;

import java.util.*;

/**
 * 
 * @author Adam Renberg <tgwizard@gmail.com>
 */
public class Activity {

    private String title;
    private String description;
    private String subject;
    private Date date;

    public Activity(String title) {
        this.title = title;
    }
    
    public Activity(String title, String subject, String description, Date date) {
        set(title, subject, description, date);
    }
    
    public void set(String title, String subject, String description, Date date) {
        this.title = title;
        this.subject = subject;
        this.description = description;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
    
    public String toString() {
        return "(" + title + ":" + date + ":" + subject + ")";
    }
}
