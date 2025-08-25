package com.solicare.app.backend.domain.enums;

public enum Role {
    MEMBER,
    SENIOR;

    public static Role fromString(String value) {
        for (Role r : Role.values()) {
            if (r.name().equalsIgnoreCase(value)) {
                return r;
            }
        }
        return null;
    }
}
