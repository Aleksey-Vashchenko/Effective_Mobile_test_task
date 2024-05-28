package com.vashchenko.task.exceptions;

import java.util.UUID;

public class EmailIsNotFoundException extends RuntimeException{
    public EmailIsNotFoundException(String email, String uuid) {
        super("Email"+email+" is not found. User: "+uuid);
    }
}
