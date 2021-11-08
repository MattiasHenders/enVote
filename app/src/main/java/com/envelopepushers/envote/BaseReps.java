package com.envelopepushers.envote;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class BaseReps {
    @SerializedName("toons")
    @Expose
    private ArrayList<Representative> reps = new ArrayList<>();

    public ArrayList<Representative> getReps() {
        return reps;
    }

    public void setReps(ArrayList<Representative> reps) {
        this.reps = reps;
    }
}
