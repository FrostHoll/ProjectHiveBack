package com.frostholl.projectHiveBack.exception.team;

public class NonTeamMemberAccessException extends RuntimeException{
    public NonTeamMemberAccessException(String message) {
        super(message);
    }

    public NonTeamMemberAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
