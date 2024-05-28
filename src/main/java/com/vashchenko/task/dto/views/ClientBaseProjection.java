package com.vashchenko.task.dto.views;

import lombok.Data;

import java.util.Date;
import java.util.List;

public interface ClientBaseProjection {
    String getFullName();

    Date getBirthDate();

    List<String> getEmails();

    List<String> getPhoneNumbers();
}
