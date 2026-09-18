package com.oficina.manutencao.infrastructure.adapters.inbound.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AprovarOrcamentoPublicoRequestDTO(
        @NotBlank String documentoCliente,
        @NotNull Boolean aprovado
) {
}
