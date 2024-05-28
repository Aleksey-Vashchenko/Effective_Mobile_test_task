package com.vashchenko.task.exceptions;

public class PhoneNumberIsNotFoundException extends RuntimeException{
    public PhoneNumberIsNotFoundException(String phone, String uuid) {
        super("Phone number "+phone+"  is not found. User: "+uuid);
    }
}
