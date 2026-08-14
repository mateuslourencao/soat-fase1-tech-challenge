package com.oficina.administrativo.domain.ports.outbound;

import com.oficina.administrativo.domain.model.Funcionario;

import java.util.List;
import java.util.Optional;

public interface FuncionarioRepositoryPort {
    Optional<Funcionario> buscarPorEmail(String email);
    Optional<Funcionario> buscarPorId(int id);
    List<Funcionario> listarTodos();
    Funcionario salvar(Funcionario funcionario);
}
