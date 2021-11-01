package com.envelopepushers.envote;

public enum EcoParty {

    LIBERAL("LIBERAL", "Party", R.drawable.ic_round_add_24,
            R.color.red_bright, R.color.red_dark, "https://liberal.ca/"),

    CONSERVATIVE("CONSERVATIVE", "Conservative", R.drawable.ic_baseline_invert_colors_24,
            R.color.blue_bright, R.color.blue_dark, "https://www.conservative.ca/"),

    NDP("NDP", "NPD", R.drawable.ic_baseline_delete_24,
            R.color.orange_bright, R.color.orange_dark, "https://www.ndp.ca/"),

    PPC("PPC", "People's Party Canada", R.drawable.ic_baseline_flash_on_24,
            R.color.purple_bright, R.color.purple_dark, "https://www.peoplespartyofcanada.ca/"),

    GREEN("GREEN", "Green Party", R.drawable.ic_baseline_invert_colors_24,
                 R.color.green_bright, R.color.green_darker, "https://www.greenparty.ca/en"),

    INDEPENDENT("INDEPENDENT", "Independent Party", R.drawable.ic_baseline_delete_24,
        R.color.dull_grey, R.color.dark_grey, "https://o.canada.com/");

    private final String partyName;
    private final String key;
    private final int icon;
    private final int colourLight;
    private final int colourDark;
    private final String partyLink;

    public String getPartyName() {
        return partyName;
    }

    public String getKey() {
        return key;
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

    public String getPartyLink() {
        return partyLink;
    }

    EcoParty (String key, String partyName, int icon, int colourLight, int colourDark, String partyLink) {
        this.key = key;
        this.partyName = partyName;
        this.icon = icon;
        this.colourLight = colourLight;
        this.colourDark = colourDark;
        this.partyLink = partyLink;
    }
}