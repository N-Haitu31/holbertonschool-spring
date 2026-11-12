package com.holberton.realestate.exception;

public class DuplicateTenantEmailException extends RuntimeException {
    public DuplicateTenantEmailException(String email) {
        super("A tenant with this email already exists: " + email);
    }
}
