package com.navi.exceptions;

public class SeatsRequiredException extends RuntimeException{
    public SeatsRequiredException(String message) {
        super(message);
    }
}
