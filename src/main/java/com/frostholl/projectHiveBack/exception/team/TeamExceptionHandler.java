package com.frostholl.projectHiveBack.exception.team;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class TeamExceptionHandler {
    @ExceptionHandler(value = {TeamNotFoundException.class})
    public ResponseEntity<Object> handleTeamNotFoundException
            (TeamNotFoundException teamNotFoundException) {
        TeamException teamException = new TeamException(
                teamNotFoundException.getMessage(),
                teamNotFoundException.getCause(),
                HttpStatus.NOT_FOUND
        );

        return new ResponseEntity<>(teamException, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {TeamAdminNotFoundException.class})
    public ResponseEntity<Object> handleTeamAdminNotFoundException
            (TeamAdminNotFoundException teamAdminNotFoundException) {
        TeamException teamException = new TeamException(
                teamAdminNotFoundException.getMessage(),
                teamAdminNotFoundException.getCause(),
                HttpStatus.NOT_FOUND
        );

        return new ResponseEntity<>(teamException, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {NonTeamMemberAccessException.class})
    public ResponseEntity<Object> handleNonTeamMemberAccessException
            (NonTeamMemberAccessException nonTeamMemberAccessException) {
        TeamException teamException = new TeamException(
                nonTeamMemberAccessException.getMessage(),
                nonTeamMemberAccessException.getCause(),
                HttpStatus.FORBIDDEN
        );

        return new ResponseEntity<>(teamException, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = {InviteCodeNotFoundOrExpiredException.class})
    public ResponseEntity<Object> handleInviteCodeNotFoundOrExpiredException
            (InviteCodeNotFoundOrExpiredException inviteCodeNotFoundOrExpiredException) {
        TeamException teamException = new TeamException(
                inviteCodeNotFoundOrExpiredException.getMessage(),
                inviteCodeNotFoundOrExpiredException.getCause(),
                HttpStatus.NOT_FOUND
        );

        return new ResponseEntity<>(teamException, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {UserIsAlreadyMemberException.class})
    public ResponseEntity<Object> handleUserIsAlreadyMemberException
            (UserIsAlreadyMemberException userIsAlreadyMemberException) {
        TeamException teamException = new TeamException(
                userIsAlreadyMemberException.getMessage(),
                userIsAlreadyMemberException.getCause(),
                HttpStatus.CONFLICT
        );

        return new ResponseEntity<>(teamException, HttpStatus.CONFLICT);
    }
}
