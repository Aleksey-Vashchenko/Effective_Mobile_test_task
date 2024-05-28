package com.vashchenko.task.dto.requests;

import jakarta.validation.constraints.NotEmpty;

import java.math.BigDecimal;

public record SendMoneyRequest(BigDecimal money, @NotEmpty String accountTo) {
}
