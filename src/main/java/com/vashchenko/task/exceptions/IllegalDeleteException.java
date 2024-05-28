package com.vashchenko.task.exceptions;

public class IllegalDeleteException extends BaseConflictException{
    public IllegalDeleteException(String message) {
        super(message);
    }
}
