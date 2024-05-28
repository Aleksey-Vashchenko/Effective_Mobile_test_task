package com.vashchenko.task.dto.requests;

import com.vashchenko.task.validation.MoneyPrecision;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.springframework.format.annotation.DateTimeFormat;
import java.math.BigDecimal;
import java.util.Date;

public record CreateClientRequest (@NotEmpty String login,
                                   @NotEmpty String password,
                                   @MoneyPrecision BigDecimal startSum,
                                   @NotEmpty String email,
                                   @Pattern(
                                           regexp = "^(\\+375|8\\s?0)(25|29|33|44)\\s?[1-9]\\d{2}\\s?\\d{2}\\s?\\d{2}$",
                                           message = "Invalid phone number"
                                   ) String phoneNumber,
                                   @NotEmpty String name,
                                   @NotNull(message = "Birth date cannot be null")
                                   @DateTimeFormat(pattern = "yyyy-MM-dd") Date birthDate) {
}
