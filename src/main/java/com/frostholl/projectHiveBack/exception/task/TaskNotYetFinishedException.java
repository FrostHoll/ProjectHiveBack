package com.frostholl.projectHiveBack.exception.task;

public class TaskNotYetFinishedException extends RuntimeException{
    public TaskNotYetFinishedException(String message) {
        super(message);
    }

    public TaskNotYetFinishedException(String message, Throwable cause) {
        super(message, cause);
    }
}
