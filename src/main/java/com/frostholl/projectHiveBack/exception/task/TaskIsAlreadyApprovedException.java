package com.frostholl.projectHiveBack.exception.task;

public class TaskIsAlreadyApprovedException extends RuntimeException{
    public TaskIsAlreadyApprovedException(String message) {
        super(message);
    }

    public TaskIsAlreadyApprovedException(String message, Throwable cause) {
        super(message, cause);
    }
}
