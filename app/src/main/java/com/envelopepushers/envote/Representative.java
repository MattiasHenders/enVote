package com.envelopepushers.envote;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Representative {
    @SerializedName("id")
    @Expose
    private int id;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    @SerializedName("firstName")
    @Expose
    private String firstName;
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @SerializedName("lastName")
    @Expose
    private String lastName;
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @SerializedName("occupation")
    @Expose
    private String occupation;
    public String getOccupation() {
        return occupation;
    }
    public void setOccupation(String occupation) {
        this.lastName = occupation;
    }

    @SerializedName("gender")
    @Expose
    private String gender;
    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }

    @SerializedName("pictureUrl")
    @Expose
    private String pictureUrl;
    public String getPictureUrl() {
        return pictureUrl;
    }
    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    @SerializedName("votes")
    @Expose
    private int votes;
    public int getVotes() {
        return votes;
    }
    public void setVotes(int votes) {
        this.id = votes;
    }

    public Representative(int id, String firstName, String lastName, String occupation, String gender, String pictureUrl, int votes) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.occupation = occupation;
        this.gender = gender;
        this.pictureUrl = pictureUrl;
        this.votes = votes;
    }
}
