package com.oficina.estoque.domain.ports.outbound;

import com.oficina.estoque.domain.model.Servico;

import java.util.List;
import java.util.Optional;

public interface ServicoRepositoryPort {
    Servico salvar(Servico servico);
    Optional<Servico> buscarPorId(int id);
    List<Servico> listarServicos();
    Servico atualizarServico(Servico servico);
    Void removerServico(int id);
}
