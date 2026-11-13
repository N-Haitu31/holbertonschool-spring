package com.holberton.realestate.service;

public final class LeaseRules {
    public static final int MIN_DURATION_MONTHS = 12;
    public static final int MAX_DURATION_MONTHS = 36;
    public static final int INCOME_MULTIPLIER = 3;
    public static final int DEPOSIT_MONTHS = 2;

    private LeaseRules() {
        // classe utilitaire : on ne l'instancie pas
    }
}
