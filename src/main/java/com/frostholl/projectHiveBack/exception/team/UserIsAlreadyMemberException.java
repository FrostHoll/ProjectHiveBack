package com.frostholl.projectHiveBack.exception.team;

public class UserIsAlreadyMemberException extends RuntimeException{
    public UserIsAlreadyMemberException(String message) {
        super(message);
    }

    public UserIsAlreadyMemberException(String message, Throwable cause) {
        super(message, cause);
    }
}
