package com.oficina.administrativo.infrastructure.config;

import com.oficina.administrativo.application.service.AutenticarFuncionarioService;
import com.oficina.administrativo.domain.ports.inbound.AutenticarFuncionarioUseCase;
import com.oficina.administrativo.domain.ports.outbound.FuncionarioRepositoryPort;
import com.oficina.administrativo.domain.ports.outbound.SenhaCriptografadaPort;
import com.oficina.administrativo.domain.ports.outbound.TokenJwtPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FuncionarioConfig {

    @Bean
    public AutenticarFuncionarioUseCase autenticarFuncionarioUseCase(
            FuncionarioRepositoryPort funcionarioRepository,
            SenhaCriptografadaPort senhaCriptografada,
            TokenJwtPort tokenJwt) {
        return new AutenticarFuncionarioService(funcionarioRepository, senhaCriptografada, tokenJwt);
    }
}
