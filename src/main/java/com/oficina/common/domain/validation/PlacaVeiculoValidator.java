package com.oficina.common.domain.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PlacaVeiculoValidator implements ConstraintValidator<PlacaVeiculo, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return true;
        }

        // Padrão antigo: AAA-0000 ou AAA0000
        // Padrão Mercosul: AAA0A00
        String regex = "^[A-Z]{3}-?\\d{4}$|^[A-Z]{3}\\d[A-Z]\\d{2}$";
        
        return value.toUpperCase().matches(regex);
    }
}
