package com.vashchenko.task.dto.requests;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

public record ActionPhoneRequest(@NotEmpty @Pattern(
        regexp = "^375\\d{9}$",
        message = "Invalid phone number. Pattern '375xxyyyyyyy'"
        ) String phoneNumber) {
}
