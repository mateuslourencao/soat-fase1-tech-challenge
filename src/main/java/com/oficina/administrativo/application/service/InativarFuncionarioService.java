package com.oficina.administrativo.application.service;

import com.oficina.administrativo.domain.model.Funcionario;
import com.oficina.administrativo.domain.ports.inbound.InativarFuncionarioUseCase;
import com.oficina.administrativo.domain.ports.outbound.FuncionarioRepositoryPort;
import com.oficina.common.domain.exception.EntidadeNaoEncontradaException;

public class InativarFuncionarioService implements InativarFuncionarioUseCase {
    private final FuncionarioRepositoryPort funcionarioRepository;

    public InativarFuncionarioService(FuncionarioRepositoryPort funcionarioRepository) {
        this.funcionarioRepository = funcionarioRepository;
    }

    @Override
    public void inativarFuncionario(int id) {
        Funcionario existente = funcionarioRepository.buscarPorId(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Funcionário não encontrado"));
        
        Funcionario inativado = new Funcionario(
                existente.getId(), 
                existente.getNome(), 
                existente.getEmail(),
                existente.getSenhaHash(), 
                existente.getPerfil(), 
                false
        );
        
        funcionarioRepository.salvar(inativado);
    }
}
