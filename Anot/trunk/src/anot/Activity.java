/*
 */
package anot;

import java.awt.Color;

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
    private Color color;

    public Activity(String title) {
        this.title = title;
        color = Color.white;
    }
    
    public void set(String title, String subject, String description, Date date, Color color) {
        this.title = title;
        this.subject = subject;
        this.description = description;
        this.date = date;
        this.color = color;
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

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
    
    
}
