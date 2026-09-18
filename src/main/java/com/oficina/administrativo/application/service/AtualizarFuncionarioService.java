package com.oficina.administrativo.application.service;

import com.oficina.administrativo.domain.model.Funcionario;
import com.oficina.administrativo.domain.ports.inbound.AtualizarFuncionarioUseCase;
import com.oficina.administrativo.domain.ports.outbound.FuncionarioRepositoryPort;
import com.oficina.common.domain.exception.EntidadeNaoEncontradaException;

public class AtualizarFuncionarioService implements AtualizarFuncionarioUseCase {
    private final FuncionarioRepositoryPort funcionarioRepository;

    public AtualizarFuncionarioService(FuncionarioRepositoryPort funcionarioRepository) {
        this.funcionarioRepository = funcionarioRepository;
    }

    @Override
    public Funcionario atualizarFuncionario(int id, Funcionario funcionario) {
        if (funcionario == null) {
            throw new IllegalArgumentException("Dados do funcionário inválidos");
        }
        Funcionario existente = funcionarioRepository.buscarPorId(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Funcionário não encontrado"));
        funcionarioRepository.buscarPorEmail(funcionario.getEmail())
                .filter(outroFuncionario -> outroFuncionario.getId() != id)
                .ifPresent(outroFuncionario -> { throw new IllegalArgumentException("Funcionário já cadastrado com este e-mail"); });

        Funcionario atualizado = existente.atualizarDados(funcionario.getNome(), funcionario.getEmail(), funcionario.getPerfil());
        return funcionarioRepository.salvar(atualizado);
    }
}
