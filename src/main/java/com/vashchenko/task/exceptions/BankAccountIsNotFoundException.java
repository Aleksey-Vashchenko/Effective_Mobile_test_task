package com.vashchenko.task.exceptions;

public class BankAccountIsNotFoundException extends RuntimeException{
    public BankAccountIsNotFoundException() {
        super("Bank account is not found");
    }
}
