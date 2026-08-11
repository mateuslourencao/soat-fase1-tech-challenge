package com.oficina.manutencao.domain.ports.inbound;

import com.oficina.manutencao.domain.model.OrdemDeServico;

import java.util.UUID;

public interface AprovarOrcamentoUseCase {
        void AprovarOrcamento(UUID ordemDeServicoID);
}
