package com.frostholl.projectHiveBack.exception.auth;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AuthenticationExceptionHandler {

    @ExceptionHandler(value = {UserLoginAlreadyInUseException.class})
    public ResponseEntity<Object> handleUserLoginAlreadyInUseException
            (UserLoginAlreadyInUseException userLoginAlreadyInUseException) {
        AuthenticationException authenticationException = new AuthenticationException(
                userLoginAlreadyInUseException.getMessage(),
                userLoginAlreadyInUseException.getCause(),
                HttpStatus.CONFLICT
        );

        return new ResponseEntity<>(authenticationException, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = {UserNotFoundException.class})
    public ResponseEntity<Object> handleUserNotFoundException
            (UserNotFoundException userNotFoundException) {
        AuthenticationException authenticationException = new AuthenticationException(
                userNotFoundException.getMessage(),
                userNotFoundException.getCause(),
                HttpStatus.NOT_FOUND
        );

        return new ResponseEntity<>(authenticationException, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {org.springframework.security.core.AuthenticationException.class})
    public ResponseEntity<Object> handleAuthenticationException
            (org.springframework.security.core.AuthenticationException authenticationException) {
        AuthenticationException authException = new AuthenticationException(
                authenticationException.getMessage(),
                authenticationException.getCause(),
                HttpStatus.UNAUTHORIZED
        );

        return new ResponseEntity<>(authException, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<Object> handleExpiredJwtException
            (ExpiredJwtException expiredJwtException) {
        AuthenticationException authException = new AuthenticationException(
                expiredJwtException.getMessage(),
                expiredJwtException.getCause(),
                HttpStatus.FORBIDDEN
        );

        return new ResponseEntity<>(authException, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = {IncorrectUserDataException.class})
    public ResponseEntity<Object> handleIncorrectUserDataException
            (IncorrectUserDataException incorrectUserDataException) {
        AuthenticationException authException = new AuthenticationException(
                incorrectUserDataException.getMessage(),
                incorrectUserDataException.getCause(),
                HttpStatus.BAD_REQUEST
        );

        return new ResponseEntity<>(authException, HttpStatus.BAD_REQUEST);
    }
}
