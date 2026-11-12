package com.holberton.realestate.exception;

public class DuplicateReferenceException extends RuntimeException {
    public DuplicateReferenceException(String reference) {
        super("A property with this reference already exists: " + reference);
    }
}
