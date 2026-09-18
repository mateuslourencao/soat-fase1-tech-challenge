package com.oficina.estoque.application.service;

import com.oficina.common.domain.exception.EntidadeNaoEncontradaException;
import com.oficina.estoque.domain.model.Peca;
import com.oficina.estoque.domain.ports.inbound.*;
import com.oficina.estoque.domain.ports.outbound.PecaRepositoryPort;

import java.util.List;

public class PecaService implements CadastrarPecaUseCase,
        ObterPecaUseCase, ReporPecaUseCase, ListarPecaUseCase,
        AtualizarPecaUseCase, RemoverPecaUseCase {
    private final PecaRepositoryPort pecaRepository;

    public PecaService(PecaRepositoryPort pecaRepository) {
        this.pecaRepository = pecaRepository;
    }

    public Peca cadastrarPeca(String descricao, Double valor, int quantidade) {
        validarCadastro(descricao, valor, quantidade);
        return pecaRepository.salvar(new Peca(descricao, valor, quantidade));
    }

    public Peca obterPeca(int id, int quantidadeBaixar) {
        Peca.validarQuantidadeParaBaixa(quantidadeBaixar);
        Peca peca = buscaPeca(id);
        peca.baixarEstoque(quantidadeBaixar);
        return pecaRepository.salvar(peca);
    }

    private Peca buscaPeca(int id) {
        return pecaRepository.buscarPorId(id).orElseThrow(() -> new EntidadeNaoEncontradaException("Peça não encontrada"));
    }
    private void validarCadastro(String descricao, Double valor, int quantidade) {
        if (descricao == null || descricao.isBlank() || valor == null || valor < 0 || quantidade < 0) throw new IllegalArgumentException("Dados da peça inválidos");
    }
    public Peca reporEstoque(int pecaID, int quantidadeRepor) {
        Peca.validarQuantidadeParaReposicao(quantidadeRepor);
        Peca peca = buscaPeca(pecaID);
        peca.reporEstoque(quantidadeRepor);
        return pecaRepository.salvar(peca);
    }

    @Override
    public List<Peca> listarPecas() {
        return pecaRepository.listarPecas();
    }

    @Override
    public Peca atualizarPeca(Peca peca) {
        buscaPeca(peca.getId());
        validarCadastro(peca.getDescricao(), peca.getValor(), peca.getQuantidade());
        return pecaRepository.salvar(peca);
    }

    @Override
    public void removerPeca(int id) {
        buscaPeca(id);
        pecaRepository.remover(id);
    }
}
