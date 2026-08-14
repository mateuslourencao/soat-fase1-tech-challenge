package com.oficina.manutencao.infrastructure.adapters.inbound.rest.dto;

import jakarta.validation.constraints.NotBlank;

public record CriarOrdemDeServicoRequestDTO(
        @NotBlank String documentoCliente,
        @NotBlank String placaVeiculo,
        @NotBlank String descricaoQueixas
) {}
