package com.oficina.estoque.infrastructure.adapters.inbound.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record ServicoRequestDTO(
        @NotBlank(message = "A descricao do servico e obrigatoria")
        String descricao,
        @NotNull(message = "O valor do servico e obrigatorio")
        @PositiveOrZero(message = "O valor do servico nao pode ser negativo")
        Double valor
) {
}
