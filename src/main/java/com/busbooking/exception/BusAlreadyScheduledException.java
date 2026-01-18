package com.busbooking.exception;

public class BusAlreadyScheduledException extends RuntimeException {
    public BusAlreadyScheduledException(String message) {
        super(message);
    }
}
