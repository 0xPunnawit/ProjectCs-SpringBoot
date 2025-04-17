package com.example.Tutorials_Eom.entity;

import lombok.experimental.Accessors;

@Accessors(chain = true)
public enum ProductStatus {
    OPEN_SALE("OPEN_SALE"),
    CLOSE_SALE("CLOSE_SALE"),
    STOP_SALE("STOP_SALE");

    private final String status;

    ProductStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public static ProductStatus fromString(String status) {
        for (ProductStatus ps : ProductStatus.values()) {
            if (ps.status.equalsIgnoreCase(status)) {
                return ps;
            }
        }
        throw new IllegalArgumentException("No enum constant for status: " + status);
    }
}
