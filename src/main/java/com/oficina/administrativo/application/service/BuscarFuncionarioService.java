package com.oficina.administrativo.application.service;

import com.oficina.administrativo.domain.model.Funcionario;
import com.oficina.administrativo.domain.ports.inbound.BuscarFuncionarioUseCase;
import com.oficina.administrativo.domain.ports.outbound.FuncionarioRepositoryPort;

public class BuscarFuncionarioService implements BuscarFuncionarioUseCase {
    private final FuncionarioRepositoryPort funcionarioRepository;

    public BuscarFuncionarioService(FuncionarioRepositoryPort funcionarioRepository) {
        this.funcionarioRepository = funcionarioRepository;
    }

    @Override
    public Funcionario buscarFuncionario(int id) {
        return funcionarioRepository.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Funcionário não encontrado"));
    }
}
