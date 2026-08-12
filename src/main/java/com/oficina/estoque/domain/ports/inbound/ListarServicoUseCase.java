package com.oficina.estoque.domain.ports.inbound;

import com.oficina.estoque.domain.model.Servico;

import java.util.List;

public interface ListarServicoUseCase {
    List<Servico> listarServico();
}
