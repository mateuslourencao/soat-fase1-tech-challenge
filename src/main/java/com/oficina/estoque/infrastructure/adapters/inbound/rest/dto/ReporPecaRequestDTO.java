package com.oficina.estoque.infrastructure.adapters.inbound.rest.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record ReporPecaRequestDTO(
        @NotNull(message = "O id da peca e obrigatorio")
        int id,
        @PositiveOrZero(message = "A quantidade da peca nao pode ser negativa")
        int quantidade
) {
}
