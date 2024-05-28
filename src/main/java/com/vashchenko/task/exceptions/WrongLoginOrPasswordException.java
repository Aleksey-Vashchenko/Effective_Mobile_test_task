package com.vashchenko.task.exceptions;

public class WrongLoginOrPasswordException extends RuntimeException{
    public WrongLoginOrPasswordException() {
        super("Wrong login or password");
    }
}
