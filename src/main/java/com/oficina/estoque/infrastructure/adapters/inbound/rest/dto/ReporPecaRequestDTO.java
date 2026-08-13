package com.oficina.estoque.infrastructure.adapters.inbound.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

public record ReporPecaRequestDTO(
        @NotBlank(message = "O id da peca e obrigatorio")
        int id,
        @PositiveOrZero(message = "A quantidade da peca nao pode ser negativa")
        int quantidade
) {
}
