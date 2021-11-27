package com.envelopepushers.envote;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Representative {

    /**
     * Name of the rep.
     */
    @SerializedName("name")
    @Expose
    private String name;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    /**
     * email of the rep.
     */
    @SerializedName("email")
    @Expose
    private String email;
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Party of the rep.
     */
    @SerializedName("party")
    @Expose
    private String party;
    public String getParty() {
        return party;
    }
    public void setParty(String party) {
        this.party = party;
    }

    /**
     * Government level of the rep.
     */
    @SerializedName("governmentLevel")
    @Expose
    private String governmentLevel;
    public String getGovernmentLevel() {
        return governmentLevel;
    }
    public void setGovernmentLevel(String gov) {
        this.governmentLevel = governmentLevel;
    }

    /**
     * URL of the rep.
     */
    @SerializedName("pictureUrl")
    @Expose
    private String pictureUrl;
    public String getPictureUrl() {
        return pictureUrl;
    }
    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    /**
     * Constructs the Representative object.
     * @param name String
     * @param party String
     * @param email String
     * @param governmentLevel String
     * @param pictureUrl String
     */
    public Representative(String name, String party, String email, String governmentLevel, String pictureUrl) {
        this.name = name;
        this.party = party;
        this.email = email;
        this.governmentLevel = governmentLevel;
        this.pictureUrl = pictureUrl;
    }
}
