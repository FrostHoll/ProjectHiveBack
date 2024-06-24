package com.frostholl.projectHiveBack.exception.task;

public class TaskIsAlreadyTakenException extends RuntimeException{
    public TaskIsAlreadyTakenException(String message) {
        super(message);
    }

    public TaskIsAlreadyTakenException(String message, Throwable cause) {
        super(message, cause);
    }
}
