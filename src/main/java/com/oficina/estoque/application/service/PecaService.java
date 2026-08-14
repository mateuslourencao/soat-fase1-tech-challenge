package com.oficina.estoque.application.service;

import com.oficina.estoque.domain.model.Peca;
import com.oficina.estoque.domain.ports.inbound.CadastrarPecaUseCase;
import com.oficina.estoque.domain.ports.inbound.ListarPecaUseCase;
import com.oficina.estoque.domain.ports.inbound.ObterPecaUseCase;
import com.oficina.estoque.domain.ports.inbound.ReporPecaUseCase;
import com.oficina.estoque.domain.ports.outbound.PecaRepositoryPort;

import java.util.List;

public class PecaService implements CadastrarPecaUseCase,
        ObterPecaUseCase, ReporPecaUseCase, ListarPecaUseCase {
    private final PecaRepositoryPort pecaRepository;

    public PecaService(PecaRepositoryPort pecaRepository) {
        this.pecaRepository = pecaRepository;
    }

    public Peca cadastrarPeca(String descricao, Double valor, int quantidade) {
        validarCadastro(descricao, valor, quantidade);
        return pecaRepository.salvar(new Peca(descricao, valor, quantidade));
    }

    public Peca obterPeca(int id, int quantidadeBaixar) {
        if (quantidadeBaixar <= 0) throw new IllegalArgumentException("Quantidade para baixa deve ser positiva");
        Peca peca = buscaPeca(id);
        if (peca.getQuantidade() < quantidadeBaixar) throw new IllegalStateException("Estoque insuficiente");
        peca.atualizarQuantidade(peca.getQuantidade() - quantidadeBaixar);
        return pecaRepository.salvar(peca);
    }

    private Peca buscaPeca(int id) {
        return pecaRepository.buscarPorId(id).orElseThrow(() -> new IllegalArgumentException("Peça não encontrada"));
    }
    private void validarCadastro(String descricao, Double valor, int quantidade) {
        if (descricao == null || descricao.isBlank() || valor == null || valor < 0 || quantidade < 0) throw new IllegalArgumentException("Dados da peça inválidos");
    }

    public Peca reporEstoque(int pecaID, int quantidadeRepor) {
        if (quantidadeRepor <= 0) throw new IllegalArgumentException("Quantidade para repor deve ser positiva");
        Peca peca = buscaPeca(pecaID);
        peca.atualizarQuantidade(peca.getQuantidade() + quantidadeRepor);
        return pecaRepository.salvar(peca);
    }

    @Override
    public List<Peca> listarPecas() {
        List<Peca> pecas = pecaRepository.listarPecas();
        return pecas;
    }
}
