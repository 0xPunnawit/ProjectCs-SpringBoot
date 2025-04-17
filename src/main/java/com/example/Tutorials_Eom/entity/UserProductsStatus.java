package com.example.Tutorials_Eom.entity;

import lombok.experimental.Accessors;

@Accessors(chain = true)
public enum UserProductsStatus {
    ACTIVATE("ACTIVATE"),
    DEACTIVATE("DEACTIVATE");

    private final String status;

    UserProductsStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }


    public static UserProductsStatus fromString(String status) {
        for (UserProductsStatus ps : UserProductsStatus.values()) {
            if (ps.status.equalsIgnoreCase(status)) {
                return ps;
            }
        }
        throw new IllegalArgumentException("No enum constant for status: " + status);
    }
}
