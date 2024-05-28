package com.vashchenko.task.dto.requests;

import jakarta.validation.constraints.NotEmpty;

public record AuthorizationRequest(@NotEmpty String login,@NotEmpty String password) {
}
