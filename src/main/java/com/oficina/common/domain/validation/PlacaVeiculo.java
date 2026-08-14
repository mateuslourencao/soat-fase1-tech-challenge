package com.oficina.common.domain.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PlacaVeiculoValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface PlacaVeiculo {
    String message() default "Placa de veículo inválida";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
