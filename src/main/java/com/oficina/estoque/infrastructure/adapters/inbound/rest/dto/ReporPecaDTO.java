package com.oficina.estoque.infrastructure.adapters.inbound.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.UUID;

public record ReporPecaDTO(
        @NotBlank(message = "O id da peca e obrigatorio")
        UUID id,
        @PositiveOrZero(message = "A quantidade da peca nao pode ser negativa")
        int quantidade
) {
}
