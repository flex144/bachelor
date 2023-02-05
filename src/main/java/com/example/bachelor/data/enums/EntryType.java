package com.example.bachelor.data.enums;

public enum EntryType {
    DEFAULT("Meldung"),
    INCIDENT("Ereignis"),
    WEATHER("Wetter"),
    WATER_TEMP("Wassertemperatur"),
    USER_GUARD_BEGIN("Wachanmeldung"),
    USER_GUARD_END("Wachabmeldung"),
    GUARD_BEGIN("Wachbeginn"),
    GUARD_END("Wachende"),
    ILS_ACTIVE("Anmeldung ILS"),
    ILS_INACTIVE("Abmeldung ILS");

    private final String description;

    EntryType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
