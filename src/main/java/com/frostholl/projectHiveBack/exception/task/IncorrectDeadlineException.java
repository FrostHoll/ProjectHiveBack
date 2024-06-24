package com.frostholl.projectHiveBack.exception.task;

public class IncorrectDeadlineException extends RuntimeException{
    public IncorrectDeadlineException(String message) {
        super(message);
    }

    public IncorrectDeadlineException(String message, Throwable cause) {
        super(message, cause);
    }
}
