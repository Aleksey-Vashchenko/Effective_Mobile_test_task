package com.vashchenko.task.exceptions;

public class UserIsAlreadyExistsException extends BaseConflictException {
    public UserIsAlreadyExistsException() {
        super("User with this login is already exists");
    }
}
