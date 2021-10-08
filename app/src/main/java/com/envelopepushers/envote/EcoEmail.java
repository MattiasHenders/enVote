package com.envelopepushers.envote;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.util.GeoPoint;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class EcoEmail {

    /** JSONObject used */
    private JSONObject emailObject;

    /**
     * EcoEmail Default Constructor
     */
    public EcoEmail() {
        emailObject = new JSONObject();
    }

    /**
     * EcoEmail Constructor
     * @param emailObject
     */
    public EcoEmail(JSONObject emailObject) {
        this.emailObject = emailObject;
    }

    /**
     * Sets the email subject
     * @param subject
     */
    public void setSubject(String subject) {
        try {
            emailObject.put("email", subject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the subject
     * @return subject String
     */
    public String getSubject() {
        try {
            return emailObject.get("email").toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Sets the date
     * @param date
     */
    public void setDate(Date date) {

        SimpleDateFormat formater = new SimpleDateFormat("MMM dd yyyy");
        String strDate = formater.format(date);
        try {
            emailObject.put("dateSent", strDate);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the date
     * @return date String
     */
    public String getDate() {
        try {
            return emailObject.get("dateSent").toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Sets the location
     * @param location
     */
    public void setLocation(GeoPoint location) {

        double lat = location.getLatitude();
        double lon = location.getLongitude();

        try {
            emailObject.put("latitude", ("" + lat));
            emailObject.put("longitude", ("" + lon));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the location
     * @return location GeoPoint
     */
    public GeoPoint getLocation() {

        double lat;
        double lon;

        try {
            lat = Double.parseDouble(emailObject.get("latitude").toString());
            lon = Double.parseDouble(emailObject.get("longitude").toString());
        } catch (Exception e) {
            e.printStackTrace();
            lat = 0;
            lon = 0;
        }

        return new GeoPoint(lat, lon);
    }

    /**
     * Sets the people sent to
     * @param person
     */
    public void addDeliveredTo(EmailReceiver person) {

        JSONArray deliveredTo = new JSONArray();

        try {
            JSONObject holder = new JSONObject();
            holder.put("email", person.getEmail());
            holder.put("name", person.getFullName());
            holder.put("party", person.getParty());
            deliveredTo.put(holder);
            emailObject.put("deliveredTo", deliveredTo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the date
     * @return date String
     */
    public ArrayList<EmailReceiver> getDeliveredTo() {

        ArrayList<EmailReceiver> returnArray = new ArrayList<>();
        JSONArray deliveredToArr = null;

        try {
            deliveredToArr = (JSONArray) emailObject.get("deliveredTo");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Empty array check
        if (deliveredToArr.length() == 0) {
            returnArray.add(new EmailReceiver());
            return returnArray;
        }

        try {
            for (int i = 0; i < deliveredToArr.length(); i++) {

                JSONObject deliveredTo = deliveredToArr.getJSONObject(i);


                returnArray.add(
                        new EmailReceiver(
                                deliveredTo.getString("email"),
                                deliveredTo.getString("name"),
                                deliveredTo.getString("party")));
            }

        } catch (JSONException e) {
            e.printStackTrace();
            return returnArray;
        }
        return returnArray;
    }

    /**
     * Sets the issue in the JSON
     * @param issue
     */
    public void addEcoIssue(EcoIssue issue) {

        JSONArray ecoIssues = new JSONArray();

        try {
            JSONObject holder = new JSONObject();
            holder.put("name", issue.getName());
            holder.put("icon", issue.getIcon());
            holder.put("colour", issue.getColour());

            emailObject.put("ecoIssues", ecoIssues.put(holder));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the eco issues
     * @return EcoIssues
     */
    public ArrayList<EcoIssue> getEcoIssues() {

        ArrayList<EcoIssue> returnArray = new ArrayList<>();
        JSONArray ecoIssuesArr;

        try {
            ecoIssuesArr = (JSONArray) emailObject.get("ecoIssues");

            //Empty array check
            if (ecoIssuesArr.length() == 0) {
                returnArray.add(new EcoIssue());
                return returnArray;
            }

            for (int i = 0; i < ecoIssuesArr.length(); i++) {

                JSONObject ecoIssue = (JSONObject) ecoIssuesArr.get(i);

                returnArray.add(
                        new EcoIssue(
                                ecoIssue.getString("name"),
                                ecoIssue.getInt("icon"),
                                ecoIssue.getInt("colour")));
            }

        } catch (JSONException e) {
            e.printStackTrace();
            return returnArray;
        }
        return returnArray;
    }

    /**
     * Sets the email body
     * @param body
     */
    public void setBody(String body) {
        try {
            emailObject.put("body", body);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the body
     * @return body String
     */
    public String getBody() {
        try {
            return emailObject.get("body").toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String toString() {
        try {
            return "EcoEmail{" +
                    "emailObject=" + emailObject.toString(3) +
                    '}';
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "ERROR: TOSTRING";
    }

    /*

    {
        subject: "Test Subject",

        dateSent: "2021-09-30",

        latitude: "49.6515",
        longitude: "-120.54769",

        deliveredTo: [
                    {email: "email1@gmail.com",
                     name: Joe Mama,
                     party: "Liberals"},
                    {email: "email2@gmail.com",
                     name: "Mike Hunt",
                     party: "Conservatives"}
                    ],

        ecoIssues: [
                    {name: "Garbage Collection",
                     icon: "ic_baseline_eco_24.xml"},
                    {name: "Water Stations",
                     icon: "ic_baseline_email_24.xml"}
                    ],

        body: "Body text here..."
    }

     */
}
