package com.bean.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum OfferLetterStatus {
    SENT("Sent"),
    ACCEPTED("Accepted"),
    DECLINED("Declined");

    private final String displayName;

    OfferLetterStatus(String displayName) {
        this.displayName = displayName;
    }

    @JsonValue
    public String getDisplayName() {
        return displayName;
    }

    @JsonCreator
    public static OfferLetterStatus fromString(String value) {
        if (value == null) return null;
        for (OfferLetterStatus s : values()) {
            if (s.displayName.equalsIgnoreCase(value) || s.name().equalsIgnoreCase(value)) {
                return s;
            }
        }
        throw new IllegalArgumentException("Unknown OfferLetterStatus: " + value);
    }
}
