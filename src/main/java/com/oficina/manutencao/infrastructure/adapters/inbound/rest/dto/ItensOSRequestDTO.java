package com.oficina.manutencao.infrastructure.adapters.inbound.rest.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public record ItensOSRequestDTO(
        List<PecaItemRequest> pecasNecessarias,
        List<Integer> servicosIds
) {
    public record PecaItemRequest(
            @NotNull Integer pecaId,
            @Positive int quantidade
    ) {}
}
