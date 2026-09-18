package com.oficina.administrativo.application.service;

import com.oficina.administrativo.domain.model.Funcionario;
import com.oficina.administrativo.domain.ports.inbound.CadastrarFuncionarioUseCase;
import com.oficina.administrativo.domain.ports.outbound.FuncionarioRepositoryPort;
import com.oficina.administrativo.domain.ports.outbound.SenhaCriptografadaPort;

public class CadastrarFuncionarioService implements CadastrarFuncionarioUseCase {
    private final FuncionarioRepositoryPort funcionarioRepository;
    private final SenhaCriptografadaPort senhaCriptografada;

    public CadastrarFuncionarioService(FuncionarioRepositoryPort funcionarioRepository, SenhaCriptografadaPort senhaCriptografada) {
        this.funcionarioRepository = funcionarioRepository;
        this.senhaCriptografada = senhaCriptografada;
    }

    @Override
    public Funcionario cadastrarFuncionario(Funcionario funcionario, String senha) {
        if (funcionario == null || senha == null || senha.isBlank()) {
            throw new IllegalArgumentException("Dados do funcionário inválidos");
        }
        funcionarioRepository.buscarPorEmail(funcionario.getEmail()).ifPresent(existente -> {
            throw new IllegalArgumentException("Funcionário já cadastrado com este e-mail");
        });
        Funcionario novoFuncionario = new Funcionario(0, funcionario.getNome(), funcionario.getEmail(),
                senhaCriptografada.criptografar(senha), funcionario.getPerfil(), true);
        return funcionarioRepository.salvar(novoFuncionario);
    }
}
