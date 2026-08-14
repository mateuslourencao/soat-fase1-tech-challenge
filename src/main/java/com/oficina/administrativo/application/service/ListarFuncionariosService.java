package com.oficina.administrativo.application.service;

import com.oficina.administrativo.domain.model.Funcionario;
import com.oficina.administrativo.domain.ports.inbound.ListarFuncionariosUseCase;
import com.oficina.administrativo.domain.ports.outbound.FuncionarioRepositoryPort;

import java.util.List;

public class ListarFuncionariosService implements ListarFuncionariosUseCase {
    private final FuncionarioRepositoryPort funcionarioRepository;

    public ListarFuncionariosService(FuncionarioRepositoryPort funcionarioRepository) {
        this.funcionarioRepository = funcionarioRepository;
    }

    @Override
    public List<Funcionario> listarFuncionarios() {
        return funcionarioRepository.listarTodos();
    }
}
