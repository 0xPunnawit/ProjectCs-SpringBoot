package com.example.Tutorials_Eom.entity;

import lombok.experimental.Accessors;

@Accessors(chain = true)
public enum TransactionStatus {
    PENDING("PENDING"),
    SUCCESS("SUCCESS"),
    FAILED("FAILED");

    private final String status;

    TransactionStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public static TransactionStatus fromString(String status) {
        for (TransactionStatus ts : TransactionStatus.values()) {
            if (ts.status.equalsIgnoreCase(status)) {
                return ts;
            }
        }
        throw new IllegalArgumentException("No enum constant for status: " + status);
    }
}
