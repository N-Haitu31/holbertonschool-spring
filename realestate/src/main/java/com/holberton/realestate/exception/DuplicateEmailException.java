package com.holberton.realestate.exception;

public class DuplicateEmailException extends RuntimeException {
    public DuplicateEmailException(String email) {
        super("An owner with this email already exists: " + email);
    }
}
