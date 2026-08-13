package com.oficina.administrativo.application.service;

import com.oficina.administrativo.domain.model.Funcionario;
import com.oficina.administrativo.domain.model.FuncionarioAutenticado;
import com.oficina.administrativo.domain.ports.inbound.AutenticarFuncionarioUseCase;
import com.oficina.administrativo.domain.ports.outbound.FuncionarioRepositoryPort;
import com.oficina.administrativo.domain.ports.outbound.SenhaCriptografadaPort;
import com.oficina.administrativo.domain.ports.outbound.TokenJwtPort;

public class AutenticarFuncionarioService implements AutenticarFuncionarioUseCase {
    private static final String CREDENCIAIS_INVALIDAS = "Credenciais invalidas";

    private final FuncionarioRepositoryPort funcionarioRepository;
    private final SenhaCriptografadaPort senhaCriptografada;
    private final TokenJwtPort tokenJwt;

    public AutenticarFuncionarioService(FuncionarioRepositoryPort funcionarioRepository,
                                        SenhaCriptografadaPort senhaCriptografada,
                                        TokenJwtPort tokenJwt) {
        this.funcionarioRepository = funcionarioRepository;
        this.senhaCriptografada = senhaCriptografada;
        this.tokenJwt = tokenJwt;
    }

    @Override
    public FuncionarioAutenticado autenticar(String email, String senha) {
        if (email == null || email.isBlank() || senha == null || senha.isBlank()) {
            throw new IllegalArgumentException(CREDENCIAIS_INVALIDAS);
        }

        Funcionario funcionario = funcionarioRepository.buscarPorEmail(email.trim().toLowerCase())
                .orElseThrow(() -> new IllegalArgumentException(CREDENCIAIS_INVALIDAS));

        if (!funcionario.isAtivo() || !senhaCriptografada.confere(senha, funcionario.getSenhaHash())) {
            throw new IllegalArgumentException(CREDENCIAIS_INVALIDAS);
        }
        return new FuncionarioAutenticado(funcionario, tokenJwt.gerar(funcionario));
    }
}
