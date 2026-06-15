package com.servicecrm.model.entity;

public enum UserRole {
    ADMIN("ROLE_ADMIN"),
    MANAGER("ROLE_MANAGER"),
    MASTER("ROLE_MASTER"),
    CLIENT("ROLE_CLIENT");
    
    private final String role;
    
    UserRole(String role) {
        this.role = role;
    }
    
    public String getRole() {
        return role;
    }
}
