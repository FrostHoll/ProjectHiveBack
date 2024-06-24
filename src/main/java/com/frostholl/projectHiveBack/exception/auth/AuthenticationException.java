package com.frostholl.projectHiveBack.exception.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public class AuthenticationException {
    private final String message;

    private final Throwable throwable;

    private final HttpStatus httpStatus;
}
