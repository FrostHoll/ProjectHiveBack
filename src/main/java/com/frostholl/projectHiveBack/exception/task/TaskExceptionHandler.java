package com.frostholl.projectHiveBack.exception.task;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class TaskExceptionHandler {
    @ExceptionHandler(value = {TaskNotFoundException.class})
    public ResponseEntity<Object> handleTaskNotFoundException
            (TaskNotFoundException taskNotFoundException) {
        TaskException taskException = new TaskException(
                taskNotFoundException.getMessage(),
                taskNotFoundException.getCause(),
                HttpStatus.NOT_FOUND
        );

        return new ResponseEntity<>(taskException, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {IncorrectDeadlineException.class})
    public ResponseEntity<Object> handleIncorrectDeadlineException
            (IncorrectDeadlineException incorrectDeadlineException) {
        TaskException taskException = new TaskException(
                incorrectDeadlineException.getMessage(),
                incorrectDeadlineException.getCause(),
                HttpStatus.BAD_REQUEST
        );

        return new ResponseEntity<>(taskException, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {UnauthorizedTaskAccessException.class})
    public ResponseEntity<Object> handleUnauthorizedTaskAccessException
            (UnauthorizedTaskAccessException unauthorizedTaskAccessException) {
        TaskException taskException = new TaskException(
                unauthorizedTaskAccessException.getMessage(),
                unauthorizedTaskAccessException.getCause(),
                HttpStatus.FORBIDDEN
        );

        return new ResponseEntity<>(taskException, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = {InsufficientRightsException.class})
    public ResponseEntity<Object> handleInsufficientRightsException
            (InsufficientRightsException insufficientRightsException) {
        TaskException taskException = new TaskException(
                insufficientRightsException.getMessage(),
                insufficientRightsException.getCause(),
                HttpStatus.FORBIDDEN
        );

        return new ResponseEntity<>(taskException, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = {TaskIsAlreadyTakenException.class})
    public ResponseEntity<Object> handleTaskIsAlreadyTakenException
            (TaskIsAlreadyTakenException taskIsAlreadyTakenException) {
        TaskException taskException = new TaskException(
                taskIsAlreadyTakenException.getMessage(),
                taskIsAlreadyTakenException.getCause(),
                HttpStatus.CONFLICT
        );

        return new ResponseEntity<>(taskException, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = {TaskIsAlreadyFinishedException.class})
    public ResponseEntity<Object> handleTaskIsAlreadyFinishedException
            (TaskIsAlreadyFinishedException taskIsAlreadyFinishedException) {
        TaskException taskException = new TaskException(
                taskIsAlreadyFinishedException.getMessage(),
                taskIsAlreadyFinishedException.getCause(),
                HttpStatus.CONFLICT
        );

        return new ResponseEntity<>(taskException, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = {TaskNotYetFinishedException.class})
    public ResponseEntity<Object> handleTaskNotYetFinishedException
            (TaskNotYetFinishedException taskNotYetFinishedException) {
        TaskException taskException = new TaskException(
                taskNotYetFinishedException.getMessage(),
                taskNotYetFinishedException.getCause(),
                HttpStatus.CONFLICT
        );

        return new ResponseEntity<>(taskException, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = {TaskIsAlreadyApprovedException.class})
    public ResponseEntity<Object> handleTaskIsAlreadyApprovedException
            (TaskIsAlreadyApprovedException taskIsAlreadyApprovedException) {
        TaskException taskException = new TaskException(
                taskIsAlreadyApprovedException.getMessage(),
                taskIsAlreadyApprovedException.getCause(),
                HttpStatus.CONFLICT
        );

        return new ResponseEntity<>(taskException, HttpStatus.CONFLICT);
    }
}
