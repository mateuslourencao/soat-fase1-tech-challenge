package com.oficina.manutencao.domain.ports.inbound;

import com.oficina.manutencao.domain.model.OrdemDeServico;

import java.util.List;

public interface ListarOrdensDeServicoUseCase {
    List<OrdemDeServico> listarOrdensDeServico();
}
