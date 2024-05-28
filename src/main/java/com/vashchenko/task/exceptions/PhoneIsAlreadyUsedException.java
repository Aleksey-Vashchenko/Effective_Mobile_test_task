package com.vashchenko.task.exceptions;

public class PhoneIsAlreadyUsedException extends BaseConflictException {
    public PhoneIsAlreadyUsedException() {
        super("Phone is already in use");
    }
}
