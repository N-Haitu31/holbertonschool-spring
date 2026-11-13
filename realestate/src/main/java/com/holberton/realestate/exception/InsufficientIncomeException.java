package com.holberton.realestate.exception;

public class InsufficientIncomeException extends RuntimeException {
    public InsufficientIncomeException(int income, int requiredIncome) {
        super("Monthly income " + income + " is lower than the required " + requiredIncome);
    }
}
