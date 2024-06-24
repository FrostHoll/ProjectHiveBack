package com.frostholl.projectHiveBack.exception.task;

public class UnauthorizedTaskAccessException extends RuntimeException{
    public UnauthorizedTaskAccessException(String message) {
        super(message);
    }

    public UnauthorizedTaskAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
