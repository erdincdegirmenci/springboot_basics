package com.ornek.springbootproje.enums;


public enum RoleTypes {
    ROLE_USER(1),
    ROLE_ADMIN(2),
    ROLE_MANAGER(3);

    private int roleId;

    RoleTypes(int roleId) {
        this.roleId = roleId;
    }

    public int getRoleId() {
        return roleId;
    }
}
