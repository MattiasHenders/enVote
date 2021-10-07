package com.envelopepushers.envote;

public class EcoIssue {

    private String name;
    private String icon;

    /**
     * Eco Issue Default Constructor
     */
    public EcoIssue() {
        name = "";
        icon = "";
    }

    /**
     * Eco Issue Constructor
     * @param name
     * @param icon
     */
    public EcoIssue(String name, String icon) {
        this.name = name;
        this.icon = icon;
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
    public String getIcon() {
        return icon;
    }

    /**
     * Set Icon filename
     * @param icon filename
     */
    public void setIcon(String icon) {
        this.icon = icon;
    }
}
