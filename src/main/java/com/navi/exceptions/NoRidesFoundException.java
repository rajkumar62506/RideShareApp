package com.navi.exceptions;

public class NoRidesFoundException extends RuntimeException{
    public NoRidesFoundException(String message) {
        super(message);
    }
}
