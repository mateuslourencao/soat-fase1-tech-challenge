package com.oficina.administrativo.application.service;

import com.oficina.common.domain.exception.EntidadeNaoEncontradaException;
import com.oficina.administrativo.domain.model.Funcionario;
import com.oficina.administrativo.domain.ports.inbound.AtualizarFuncionarioUseCase;
import com.oficina.administrativo.domain.ports.outbound.FuncionarioRepositoryPort;

public class AtualizarFuncionarioService implements AtualizarFuncionarioUseCase {
    private final FuncionarioRepositoryPort funcionarioRepository;

    public AtualizarFuncionarioService(FuncionarioRepositoryPort funcionarioRepository) {
        this.funcionarioRepository = funcionarioRepository;
    }

    @Override
    public Funcionario atualizarFuncionario(int id, Funcionario funcionario) {
        if (funcionario == null || funcionario.getNome() == null || funcionario.getNome().isBlank()
                || funcionario.getEmail() == null || funcionario.getEmail().isBlank() || funcionario.getPerfil() == null) {
            throw new IllegalArgumentException("Dados do funcionário inválidos");
        }
        Funcionario existente = funcionarioRepository.buscarPorId(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Funcionário não encontrado"));
        String email = funcionario.getEmail().trim().toLowerCase();
        funcionarioRepository.buscarPorEmail(email)
                .filter(outroFuncionario -> outroFuncionario.getId() != id)
                .ifPresent(outroFuncionario -> { throw new IllegalArgumentException("Funcionário já cadastrado com este e-mail"); });

        Funcionario atualizado = new Funcionario(existente.getId(), funcionario.getNome().trim(), email,
                existente.getSenhaHash(), funcionario.getPerfil(), existente.isAtivo());
        return funcionarioRepository.salvar(atualizado);
    }
}
