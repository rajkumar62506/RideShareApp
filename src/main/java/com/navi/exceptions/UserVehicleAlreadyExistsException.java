package com.navi.exceptions;

public class UserVehicleAlreadyExistsException extends RuntimeException{
    public UserVehicleAlreadyExistsException(String message) {
        super(message);
    }
}
