package com.frostholl.projectHiveBack.exception.team;

public class InviteCodeNotFoundOrExpiredException extends RuntimeException{
    public InviteCodeNotFoundOrExpiredException(String message) {
        super(message);
    }

    public InviteCodeNotFoundOrExpiredException(String message, Throwable cause) {
        super(message, cause);
    }
}
