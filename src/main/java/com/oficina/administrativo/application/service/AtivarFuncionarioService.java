package com.oficina.administrativo.application.service;

import com.oficina.administrativo.domain.model.Funcionario;
import com.oficina.administrativo.domain.ports.inbound.AtivarFuncionarioUseCase;
import com.oficina.administrativo.domain.ports.outbound.FuncionarioRepositoryPort;

public class AtivarFuncionarioService implements AtivarFuncionarioUseCase {
    private final FuncionarioRepositoryPort funcionarioRepository;

    public AtivarFuncionarioService(FuncionarioRepositoryPort funcionarioRepository) {
        this.funcionarioRepository = funcionarioRepository;
    }

    @Override
    public void ativarFuncionario(int id) {
        Funcionario existente = funcionarioRepository.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Funcionário não encontrado"));
        
        Funcionario ativado = new Funcionario(
                existente.getId(), 
                existente.getNome(), 
                existente.getEmail(),
                existente.getSenhaHash(), 
                existente.getPerfil(), 
                true
        );
        
        funcionarioRepository.salvar(ativado);
    }
}
