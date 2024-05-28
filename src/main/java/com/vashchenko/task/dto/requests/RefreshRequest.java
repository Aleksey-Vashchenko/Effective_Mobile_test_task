package com.vashchenko.task.dto.requests;

import jakarta.validation.constraints.NotEmpty;

public record RefreshRequest(@NotEmpty String accessToken, @NotEmpty String refreshToken) {
}
