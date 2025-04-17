package com.example.Tutorials_Eom.entity;

import lombok.experimental.Accessors;

@Accessors(chain = true)
public enum OrdersStatus {
    PENDING("PENDING"),
    COMPLETED("COMPLETED"),
    CANCELLED("CANCELLED");

    private final String status;

    OrdersStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public static OrdersStatus fromString(String status) {
        for (OrdersStatus os : OrdersStatus.values()) {
            if (os.status.equalsIgnoreCase(status)) {
                return os;
            }
        }
        throw new IllegalArgumentException("No enum constant for status: " + status);
    }
}
