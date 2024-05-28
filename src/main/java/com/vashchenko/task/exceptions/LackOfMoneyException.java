package com.vashchenko.task.exceptions;

public class LackOfMoneyException extends BaseConflictException{
    public LackOfMoneyException() {
        super("Dont have enough money at your balance");
    }
}
