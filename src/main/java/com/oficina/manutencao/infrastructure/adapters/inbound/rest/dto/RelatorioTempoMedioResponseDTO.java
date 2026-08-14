package com.oficina.manutencao.infrastructure.adapters.inbound.rest.dto;

public record RelatorioTempoMedioResponseDTO(
        int dias,
        long tempoMedio
) {}