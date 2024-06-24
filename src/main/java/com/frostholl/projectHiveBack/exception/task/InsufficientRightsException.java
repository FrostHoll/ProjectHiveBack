package com.frostholl.projectHiveBack.exception.task;

public class InsufficientRightsException extends RuntimeException{
    public InsufficientRightsException(String message) {
        super(message);
    }

    public InsufficientRightsException(String message, Throwable cause) {
        super(message, cause);
    }
}
