package com.vashchenko.task.exceptions;

public class EmailIsAlreadyInUsedException extends BaseConflictException {
    public EmailIsAlreadyInUsedException() {
        super("Email is already in use");
    }
}
