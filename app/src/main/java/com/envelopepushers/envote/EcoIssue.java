package com.envelopepushers.envote;

public class EcoIssue {

    private String key;
    private String name;
    private int icon;
    private int colourLight;
    private int colourDark;
    private String apiLink;

    /**
     * Eco Issue Constructor using ENUM
     * @param issues
     */
    public EcoIssue(EcoIssues issues) {
        this.key = issues.getKey();
        this.name = issues.getName();
        this.icon = issues.getIcon();
        this.colourLight = issues.getColourLight();
        this.colourDark = issues.getColourDark();
        this.apiLink = issues.getAPILink();
    }

    /**
     * Get Key
     * @return key
     */
    public String getKey() {
        return key;
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
    public int getColourLight() {
        return colourLight;
    }

    /**
     * Set Colour int
     * @param colourLight int
     */
    public void setColourLight(int colourLight) {
        this.colourLight = colourLight;
    }

    /**
     * Get Colour Dark int
     * @return colour int
     */
    public int getColourDark() {
        return colourDark;
    }

    /**
     * Set Colour Dark int
     * @param colourDark int
     */
    public void setColourDark(int colourDark) {
        this.colourDark = colourDark;
    }

    /**
     * Get apiLink
     * @return apiLink
     */
    public String getAPILink() {
        return apiLink;
    }

    /**
     * Set apiLink
     * @param apiLink int
     */
    public void setAPILink(String apiLink) {
        this.apiLink = apiLink;
    }
}
