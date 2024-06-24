package com.frostholl.projectHiveBack.exception.task;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public class TaskException {
    private final String message;

    private final Throwable throwable;

    private final HttpStatus httpStatus;
}
