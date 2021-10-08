package com.envelopepushers.envote;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;

public class EcoIssue {

    private String name;
    private int icon;
    private int colour;

    /**
     * Eco Issue Default Constructor
     */
    public EcoIssue() {
        name = "";
        icon = -1;
        colour = -1;
    }

    /**
     * Eco Issue Constructor using ENUM
     * @param issues
     */
    public EcoIssue(EcoIssues issues) {
        this.name = issues.getName();
        this.icon = issues.getIcon();
        this.colour = issues.getColour();
    }

    /**
     * Eco Issue Constructor
     * @param name
     * @param icon
     */
    public EcoIssue(String name, int icon, int colour) {
        this.name = name;
        this.icon = icon;
        this.colour = colour;
    }

    /**
     * Get Name
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the Name
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get Icon filename
     * @return icon location
     */
    public int getIcon() {
        return icon;
    }

    /**
     * Set Icon int
     * @param icon int
     */
    public void setIcon(int icon) {
        this.icon = icon;
    }

    /**
     * Get Colour int
     * @return colour int
     */
    public int getColour() {
        return colour;
    }

    /**
     * Set Colour int
     * @param colour int
     */
    public void setColour(int colour) {
        this.colour = colour;
    }
}
