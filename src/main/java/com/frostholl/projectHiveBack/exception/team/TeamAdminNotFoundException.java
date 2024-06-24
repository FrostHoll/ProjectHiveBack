package com.frostholl.projectHiveBack.exception.team;

public class TeamAdminNotFoundException extends RuntimeException{
    public TeamAdminNotFoundException(String message) {
        super(message);
    }

    public TeamAdminNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
