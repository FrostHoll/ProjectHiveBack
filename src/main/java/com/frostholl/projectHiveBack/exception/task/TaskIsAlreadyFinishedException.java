package com.frostholl.projectHiveBack.exception.task;

public class TaskIsAlreadyFinishedException extends RuntimeException{
    public TaskIsAlreadyFinishedException(String message) {
        super(message);
    }

    public TaskIsAlreadyFinishedException(String message, Throwable cause) {
        super(message, cause);
    }
}
