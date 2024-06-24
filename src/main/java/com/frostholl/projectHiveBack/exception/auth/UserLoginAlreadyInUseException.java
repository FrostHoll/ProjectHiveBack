package com.frostholl.projectHiveBack.exception.auth;

public class UserLoginAlreadyInUseException extends RuntimeException {

    public UserLoginAlreadyInUseException(String message) {
        super(message);
    }

    public UserLoginAlreadyInUseException(String message, Throwable cause) {
        super(message, cause);
    }
}
