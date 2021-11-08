package com.envelopepushers.envote;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Representative {

    @SerializedName("name")
    @Expose
    private String name;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @SerializedName("email")
    @Expose
    private String email;
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    @SerializedName("party")
    @Expose
    private String party;
    public String getParty() {
        return party;
    }
    public void setParty(String party) {
        this.party = party;
    }

    @SerializedName("governmentLevel")
    @Expose
    private String governmentLevel;
    public String getGovernmentLevel() {
        return governmentLevel;
    }
    public void setGovernmentLevel(String gov) {
        this.governmentLevel = governmentLevel;
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

    public Representative(String name, String party, String email, String governmentLevel, String pictureUrl) {
        this.name = name;
        this.party = party;
        this.email = email;
        this.governmentLevel = governmentLevel;
        this.pictureUrl = pictureUrl;
    }
}
