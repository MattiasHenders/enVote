package com.envelopepushers.envote;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Issue {
    @SerializedName("issueName")
    @Expose
    private String issueName;
    public String getIssueName() {
        return issueName;
    }
    public void setIssueName(String issueName) {
        this.issueName = issueName;
    }

    public void Issue(String issueName) {
        this.issueName = issueName;
    }
}
