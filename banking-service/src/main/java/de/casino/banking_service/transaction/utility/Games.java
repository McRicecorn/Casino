package de.casino.banking_service.transaction.utility;

public enum Games {

    ROULETTE("Roulette"),
    SLOTS("Slots");

    private final String displayName;

    Games(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
