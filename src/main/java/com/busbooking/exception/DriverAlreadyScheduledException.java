package com.busbooking.exception;

public class DriverAlreadyScheduledException extends RuntimeException {
    public DriverAlreadyScheduledException(String message) {
        super(message);
    }
}
