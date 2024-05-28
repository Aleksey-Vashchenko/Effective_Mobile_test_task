package com.vashchenko.task.dto.requests;

import jakarta.validation.constraints.Email;

public record ActionEmailRequest(@Email String email) {
}