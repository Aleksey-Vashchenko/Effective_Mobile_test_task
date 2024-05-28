package com.vashchenko.task.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.math.BigDecimal;

public class DoublePrecisionValidator implements ConstraintValidator<MoneyPrecision, BigDecimal> {

    @Override
    public void initialize(MoneyPrecision constraintAnnotation) {
    }

    @Override
    public boolean isValid(BigDecimal value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }

        if (value.compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }
        return value.scale() <= 2;
    }
}

