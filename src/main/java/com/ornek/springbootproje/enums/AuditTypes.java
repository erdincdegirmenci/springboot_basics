package com.ornek.springbootproje.enums;

public enum AuditTypes {
    LOGIN(1),
    LOGIN_FAILURE(2),
    LOGIN_SUCCESS(3);

    private final int id;

    AuditTypes(int id) {
        this.id = id;
    }

    public int getAuditId() {
        return id;
    }
}
