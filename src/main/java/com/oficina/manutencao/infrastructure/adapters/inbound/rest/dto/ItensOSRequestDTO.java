package com.oficina.manutencao.infrastructure.adapters.inbound.rest.dto;

import com.oficina.estoque.domain.model.Peca;
import com.oficina.estoque.domain.model.Servico;
import com.oficina.manutencao.domain.model.PecasNecessarias;

import java.util.List;

public record ItensOSRequestDTO(
        List<PecasNecessarias> pecasNecessarias,
        List<Servico> servicos
) {}
