package com.example.Tutorials_Eom.entity;

import lombok.experimental.Accessors;

@Accessors(chain = true)
public enum UserRole {
    PERSON("PERSON"),
    ADMIN("ADMIN");

    private final String status;

    UserRole(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public static UserRole fromString(String status) {
        for (UserRole ur : UserRole.values()) {
            if (ur.status.equalsIgnoreCase(status)) {
                return ur;
            }
        }
        throw new IllegalArgumentException("No enum constant for status: " + status);
    }
}
