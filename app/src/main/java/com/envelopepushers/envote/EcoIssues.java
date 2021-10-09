package com.envelopepushers.envote;

public enum EcoIssues {

    EMPTY("EMPTY", "Empty Issue", R.drawable.ic_round_add_24,
            R.color.dull_grey, R.color.dark_grey, ""),

    WATER("WATER", "Water Supply", R.drawable.ic_baseline_invert_colors_24,
            R.color.blue_bright, R.color.blue_dark, ""),

    TRASH("TRASH", "Trash Collection", R.drawable.ic_baseline_delete_24,
            R.color.green_bright, R.color.green_darker, ""),

    ELECTRIC("ELECTRIC", "Electric Stations", R.drawable.ic_baseline_flash_on_24,
            R.color.yellow_bright, R.color.yellow_darker, ""),

    AIR("AIR", "Air Quality", R.drawable.ic_baseline_air_24,
             R.color.teal_bright, R.color.teal_dark, "");

    private final String key;
    private final String name;
    private final int icon;
    private final int colourLight;
    private final int colourDark;
    private final String apiLink;

    private EcoIssues(String key, String name, int icon, int colourLight, int colourDark, String apiLink) {
        this.key = key;
        this.name = name;
        this.icon = icon;
        this.colourLight = colourLight;
        this.colourDark = colourDark;
        this.apiLink = apiLink;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public int getIcon() {
        return icon;
    }

    public int getColourLight() {
        return colourLight;
    }
    public int getColourDark() {
        return colourDark;
    }

    public String getAPILink() {
        return apiLink;
    }

}