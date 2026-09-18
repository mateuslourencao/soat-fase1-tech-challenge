package com.oficina.manutencao.infrastructure.adapters.inbound.rest.dto;

import jakarta.validation.constraints.NotNull;

public record AprovarOrcamentoRequestDTO(@NotNull Boolean aprovado) {
}
