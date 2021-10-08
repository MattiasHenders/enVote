package com.envelopepushers.envote;

public enum EcoIssues {

    WATER("Water Supply", R.drawable.ic_baseline_invert_colors_24, R.color.blue_bright),
    TRASH("Trash Collection", R.drawable.ic_baseline_delete_24, R.color.green_bright),
    ELECTRIC("Electric Stations", R.drawable.ic_baseline_flash_on_24, R.color.yellow_bright);

    private final String name;
    private final int icon;
    private final int colour;

    private EcoIssues(String name, int icon, int colour) {
        this.name = name;
        this.icon = icon;
        this.colour = colour;
    }

    public String getName() {
        return name;
    }

    public int getIcon() {
        return icon;
    }

    public int getColour() {
        return colour;
    }
}