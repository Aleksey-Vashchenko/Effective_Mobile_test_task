package com.vashchenko.task.exceptions;

public class InvalidTokenPairException extends RuntimeException{
    public InvalidTokenPairException() {
        super("Invalid token pair or expired refresh token");
    }
}
