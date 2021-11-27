package com.envelopepushers.envote;

import android.content.res.Resources;

public enum EcoIssues {

    EMPTY("EMPTY", "Empty Issue", R.drawable.ic_round_add_24,
            R.color.dull_grey, R.color.dark_grey, R.string.issue_empty),// Resources.getSystem().getString(R.string.issue_empty)),

    WATER("WATER", "Water Supply", R.drawable.ic_baseline_invert_colors_24,
            R.color.blue_bright, R.color.blue_dark, R.string.issue_water),// Resources.getSystem().getString(R.string.issue_water)),

    TRASH("TRASH", "Trash Collection", R.drawable.ic_baseline_delete_24,
            R.color.green_bright, R.color.green_darker, R.string.issue_trash),// Resources.getSystem().getString(R.string.issue_trash)),

    EMISSION("EMISSION", "Local Emissions", R.drawable.ic_baseline_flash_on_24,
            R.color.yellow_bright, R.color.yellow_darker, R.string.issue_emissions),// Resources.getSystem().getString(R.string.issue_electric)),

    AIR("AIR", "Air Quality", R.drawable.ic_baseline_air_24,
             R.color.teal_bright, R.color.teal_dark, R.string.issue_air);// Resources.getSystem().getString(R.string.issue_air));

    /**
     * Key to reference the issue type.
     */
    private final String key;

    /**
     * AThe name of the eco issue.
     */
    private final String name;

    /**
     * The icon of the eco issue.
     */
    private final int icon;

    /**
     * The primary color of the eco issue.
     */
    private final int colourLight;

    /**
     * The secondary color of the eco issue.
     */
    private final int colourDark;

    /**
     * The description of the eco issue.
     */
    private final int description;

    /**
     * Eco Issue constructor
     * @param key String
     * @param name String
     * @param icon int
     * @param colourLight int
     * @param colourDark int
     * @param description int
     */
  EcoIssues(String key, String name, int icon, int colourLight, int colourDark, int description) {
    this.key = key;
    this.name = name;
    this.icon = icon;
    this.colourLight = colourLight;
    this.colourDark = colourDark;
    this.description = description;
  }

    /**
     * Gets the key.
     * @return key
     */
  public String getKey() {
    return key;
  }

    /**
     * Gets the name.
     * @return name
     */
  public String getName() {
    return name;
  }

    /**
     * Gets the icon.
     * @return icon
     */
  public int getIcon() {
    return icon;
  }

    /**
     * Gets the primary color.
     * @return color
     */
  public int getColourLight() {
    return colourLight;
  }

    /**
     * Gets the secondary color.
     * @return color
     */
  public int getColourDark() {
    return colourDark;
  }

    /**
     * Gets the description.
     * @return description
     */
  public int getDescription() {
      return description;
  }
}