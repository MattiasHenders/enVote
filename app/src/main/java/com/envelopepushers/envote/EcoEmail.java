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

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public ArrayList<EmailReceiver> getDeliveredTo() {
        return deliveredTo;
    }

    public void setDeliveredTo(ArrayList<EmailReceiver> deliveredTo) {
        this.deliveredTo = deliveredTo;
    }

    public EcoIssue getIssue() {
        return issue;
    }

    public void setIssue(EcoIssue issue) {
        this.issue = issue;
    }

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
