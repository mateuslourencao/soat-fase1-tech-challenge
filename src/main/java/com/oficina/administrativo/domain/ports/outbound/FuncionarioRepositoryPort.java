package com.oficina.administrativo.domain.ports.outbound;

import com.oficina.administrativo.domain.model.Funcionario;

import java.util.Optional;
import java.util.List;

public interface FuncionarioRepositoryPort {
    Optional<Funcionario> buscarPorEmail(String email);
    Optional<Funcionario> buscarPorId(int id);
    List<Funcionario> listarTodos();
    Funcionario salvar(Funcionario funcionario);
}
