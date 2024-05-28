package com.vashchenko.task.exceptions;

public class UserIsNotFoundException extends RuntimeException{
    public UserIsNotFoundException(String user) {
        super("User "+user+" is not found");
    }
}
