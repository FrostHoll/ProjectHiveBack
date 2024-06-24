package com.frostholl.projectHiveBack.exception.auth;

public class IncorrectUserDataException extends RuntimeException{
    public IncorrectUserDataException(String message) {
        super(message);
    }

    public IncorrectUserDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
