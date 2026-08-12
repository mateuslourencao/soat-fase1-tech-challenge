package com.oficina.estoque.infrastructure.adapters.inbound.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record PecaRequestDTO(
        @NotBlank(message = "A descricao da peca e obrigatoria")
        String descricao,
        @NotNull(message = "O valor da peca e obrigatorio")
        @PositiveOrZero(message = "O valor da peca nao pode ser negativo")
        Double valor,
        @PositiveOrZero(message = "A quantidade da peca nao pode ser negativa")
        int quantidade
) {
}
