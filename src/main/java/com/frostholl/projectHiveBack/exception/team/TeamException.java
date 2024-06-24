package com.frostholl.projectHiveBack.exception.team;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public class TeamException {
    private final String message;

    private final Throwable throwable;

    private final HttpStatus httpStatus;
}
