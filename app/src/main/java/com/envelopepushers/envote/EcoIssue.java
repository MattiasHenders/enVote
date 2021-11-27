package com.envelopepushers.envote;

public class EcoIssue {

  private String key;
  private String name;
  private int icon;
  private int colourLight;
  private int colourDark;
  private int description;

  /**
   * Default constructor
   */
  public EcoIssue() {
    this.key = EcoIssues.EMPTY.getKey();
    this.name = EcoIssues.EMPTY.getName();
    this.icon = EcoIssues.EMPTY.getIcon();
    this.colourLight = EcoIssues.EMPTY.getColourLight();
    this.colourDark = EcoIssues.EMPTY.getColourDark();
    this.description = EcoIssues.EMPTY.getDescription();
  }

  /**
   * Eco Issue Constructor using ENUM
   *
   * @param issues
   */
  public EcoIssue(EcoIssues issues) {
    this.key = issues.getKey();
    this.name = issues.getName();
    this.icon = issues.getIcon();
    this.colourLight = issues.getColourLight();
    this.colourDark = issues.getColourDark();
    this.description = issues.getDescription();
  }

  /**
   * Get Key
   *
   * @return key
   */
  public String getKey() {
    return key;
  }

  /**
   * Get Name
   *
   * @return name
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the Name
   *
   * @param name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Get Icon filename
   *
   * @return icon location
   */
  public int getIcon() {
    return icon;
  }

  /**
   * Set Icon int
   *
   * @param icon int
   */
  public void setIcon(int icon) {
    this.icon = icon;
  }

  /**
   * Get Colour int
   *
   * @return colour int
   */
  public int getColourLight() {
    return colourLight;
  }

  /**
   * Set Colour int
   *
   * @param colourLight int
   */
  public void setColourLight(int colourLight) {
    this.colourLight = colourLight;
  }

  /**
   * Get Colour Dark int
   *
   * @return colour int
   */
  public int getColourDark() {
    return colourDark;
  }

  /**
   * Set Colour Dark int
   *
   * @param colourDark int
   */
  public void setColourDark(int colourDark) {
    this.colourDark = colourDark;
  }

  /**
   * Get description
   *
   * @return apiLink
   */
  public int getDescription() {
    return description;
  }

  /**
   * Set description
   *
   * @param description String
   */
  public void setDescription(int description) {
    this.description = description;
  }

  @Override
  public String toString() {
    return "EcoIssue{" +
            "name='" + name + '\'' +
            '}';
  }
}