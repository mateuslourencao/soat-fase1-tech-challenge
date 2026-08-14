package com.oficina.estoque.infrastructure.adapters.inbound.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record ServicoRequestDTO(
        @Schema(description = "Descrição detalhada do serviço", example = "Troca de óleo")
        @NotBlank(message = "A descricao do servico e obrigatoria")
        String descricao,
        @Schema(description = "Valor cobrado pelo serviço", example = "150.00")
        @NotNull(message = "O valor do servico e obrigatorio")
        @PositiveOrZero(message = "O valor do servico nao pode ser negativo")
        Double valor
) {
}
