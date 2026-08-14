package com.oficina.manutencao.infrastructure.adapters.inbound.rest.dto;

import com.oficina.estoque.infrastructure.adapters.inbound.rest.dto.PecaResponseDTO;
import com.oficina.manutencao.domain.model.PecasNecessarias;

public record PecaNecessariaResponseDTO(
        PecaResponseDTO peca,
        int quantidade,
        double valorUnitario,
        double valorTotal
) {
    public PecaNecessariaResponseDTO(PecasNecessarias pecasNecessarias) {
        this(
                new PecaResponseDTO(pecasNecessarias.peca()),
                pecasNecessarias.quantidade(),
                pecasNecessarias.getValorUnitario(),
                pecasNecessarias.getValorTotal()
        );
    }
}
