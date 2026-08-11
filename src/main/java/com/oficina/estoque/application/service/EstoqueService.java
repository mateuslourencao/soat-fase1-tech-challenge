package com.oficina.estoque.application.service;

import com.oficina.estoque.domain.model.Peca;
import com.oficina.estoque.domain.model.Servico;
import com.oficina.estoque.domain.ports.inbound.*;
import com.oficina.estoque.domain.ports.outbound.PecaRepositoryPort;
import com.oficina.estoque.domain.ports.outbound.ServicoRepositoryPort;
import java.util.UUID;

public class EstoqueService implements CadastrarPecaUseCase, CadastrarServicoUseCase,
        ObterPecaUseCase, ReporEstoqueUseCase {
    private final PecaRepositoryPort pecaRepository;
    private final ServicoRepositoryPort servicoRepository;

    public EstoqueService(PecaRepositoryPort pecaRepository, ServicoRepositoryPort servicoRepository) {
        this.pecaRepository = pecaRepository;
        this.servicoRepository = servicoRepository;
    }

    public Peca CadastrarPeca(String descricao, Double valor, int quantidade) {
        validarCadastro(descricao, valor, quantidade);
        return pecaRepository.salvar(new Peca(UUID.randomUUID(), descricao, valor, quantidade));
    }

    public Servico CadastrarServico(String descricao, Double valor) {
        if (descricao == null || descricao.isBlank() || valor == null || valor < 0) throw new IllegalArgumentException("Dados do serviço inválidos");
        return servicoRepository.salvar(new Servico(UUID.randomUUID(), descricao, valor));
    }

    public Peca ObtemPeca(UUID id, int quantidadeBaixar) {
        if (quantidadeBaixar <= 0) throw new IllegalArgumentException("Quantidade para baixa deve ser positiva");
        Peca peca = buscaPeca(id);
        if (peca.getQuantidade() < quantidadeBaixar) throw new IllegalStateException("Estoque insuficiente");
        peca.atualizarQuantidade(peca.getQuantidade() - quantidadeBaixar);
        return pecaRepository.salvar(peca);
    }

    private Peca buscaPeca(UUID id) {
        return pecaRepository.buscarPorId(id).orElseThrow(() -> new IllegalArgumentException("Peça não encontrada"));
    }
    private void validarCadastro(String descricao, Double valor, int quantidade) {
        if (descricao == null || descricao.isBlank() || valor == null || valor < 0 || quantidade < 0) throw new IllegalArgumentException("Dados da peça inválidos");
    }

    public Peca reporEstoque(UUID pecaID, int quantidadeRepor) {
        if (quantidadeRepor <= 0) throw new IllegalArgumentException("Quantidade para repor deve ser positiva");
        Peca peca = buscaPeca(pecaID);
        peca.atualizarQuantidade(peca.getQuantidade() + quantidadeRepor);
        return pecaRepository.salvar(peca);
    }
}
