package com.envelopepushers.envote;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.util.GeoPoint;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EcoEmail {

    /** JSONObject used */
    private String subject;
    private String date;
    private String body;
    private String latitude;
    private String longitude;
    private ArrayList<EmailReceiver> deliveredTo = new ArrayList<>();
    private EcoIssue issue;

    /**
     * EcoEmail Default Constructor
     */
    public EcoEmail() {
        issue = new EcoIssue(EcoIssues.EMPTY);
    }

    /**
     * Gets the subject.
     * @return subject
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Sets the subject.
     * @param subject string
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * Gets the date.
     * @return date
     */
    public String getDate() {
        return date;
    }

    /**
     * Sets the date.
     * @param date String
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Gets the body.
     * @return body
     */
    public String getBody() {
        return body;
    }

    /**
     * Sets the body.
     * @param body String
     */
    public void setBody(String body) {
        this.body = body;
    }

    /**
     * Gets the latitude.
     * @return latitude
     */
    public String getLatitude() {
        return latitude;
    }

    /**
     * Sets the latitude.
     * @param latitude String
     */
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    /**
     * Gets the longitude.
     * @return longitude
     */
    public String getLongitude() {
        return longitude;
    }

    /**
     * Sets the longitude.
     * @param longitude string
     */
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    /**
     * Gets the Delivered To.
     * @return delivered to
     */
    public ArrayList<EmailReceiver> getDeliveredTo() {
        return deliveredTo;
    }

    /**
     * Sets the delivered to.
     * @param deliveredTo ArrayList
     */
    public void setDeliveredTo(ArrayList<EmailReceiver> deliveredTo) {
        this.deliveredTo = deliveredTo;
    }

    /**
     * Gets the eco issue object.
      * @return eco issue
     */
    public EcoIssue getIssue() {
        return issue;
    }

    /**
     * Sets the eco issue.
     * @param issue EcoIssue
     */
    public void setIssue(EcoIssue issue) {
        this.issue = issue;
    }

    /**
     * Overridden to string method.
     * @return to string of the object
     */
    @Override
    public String toString() {
        return "EcoEmail{" +
                "subject='" + subject + '\'' +
                ", date='" + date + '\'' +
                ", body='" + body + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", deliveredTo=" + deliveredTo +
                ", issue=" + issue +
                '}';
    }
}
