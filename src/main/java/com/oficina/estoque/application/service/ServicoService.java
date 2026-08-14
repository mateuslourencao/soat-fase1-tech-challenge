package com.oficina.estoque.application.service;

import com.oficina.estoque.domain.model.Servico;
import com.oficina.estoque.domain.ports.inbound.*;
import com.oficina.estoque.domain.ports.outbound.ServicoRepositoryPort;

import java.util.List;

public class ServicoService implements ListarServicoUseCase, AtualizarServicoUseCase,
        RemoverServicoUseCase, CadastrarServicoUseCase, BuscarServicoUseCase {

    private final ServicoRepositoryPort servicoRepository;

    public ServicoService(ServicoRepositoryPort servicoRepository) {
        this.servicoRepository = servicoRepository;
    }

    @Override
    public List<Servico> listarServico() {
        List<Servico> servicos = servicoRepository.listarServicos();
        return servicos;
    }

    public Servico cadastrarServico(String descricao, Double valor) {
        if (descricao == null || descricao.isBlank() || valor == null || valor < 0) throw new IllegalArgumentException("Dados do serviço inválidos");
        return servicoRepository.salvar(descricao, valor);
    }

    @Override
    public Servico atualizarServico(Servico servico) {
        return servicoRepository.atualizarServico(servico);
    }

    public Servico buscarServico(int id) {
        return servicoRepository.buscarPorId(id).orElseThrow(() -> new IllegalArgumentException("Servico não encontrado"));
    }

    @Override
    public Void removerServico(int idServico) {
        Servico servico = servicoRepository.buscarPorId(idServico).orElseThrow(() -> new IllegalArgumentException("Servico não encontrado"));
        servicoRepository.deletarServico(servico.getId());
        return null;
    }
}
