package com.oficina.administrativo.infrastructure.config;

import com.oficina.administrativo.application.service.*;
import com.oficina.administrativo.domain.ports.inbound.*;
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

    @Bean
    public CadastrarFuncionarioUseCase cadastrarFuncionarioUseCase(
            FuncionarioRepositoryPort funcionarioRepository,
            SenhaCriptografadaPort senhaCriptografada) {
        return new CadastrarFuncionarioService(funcionarioRepository, senhaCriptografada);
    }

    @Bean
    public BuscarFuncionarioUseCase buscarFuncionarioUseCase(FuncionarioRepositoryPort funcionarioRepository) {
        return new BuscarFuncionarioService(funcionarioRepository);
    }

    @Bean
    public ListarFuncionariosUseCase listarFuncionariosUseCase(FuncionarioRepositoryPort funcionarioRepository) {
        return new ListarFuncionariosService(funcionarioRepository);
    }

    @Bean
    public AtualizarFuncionarioUseCase atualizarFuncionarioUseCase(FuncionarioRepositoryPort funcionarioRepository) {
        return new AtualizarFuncionarioService(funcionarioRepository);
    }

    @Bean
    public AtivarFuncionarioUseCase ativarFuncionarioUseCase(FuncionarioRepositoryPort funcionarioRepository) {
        return new AtivarFuncionarioService(funcionarioRepository);
    }

    @Bean
    public InativarFuncionarioUseCase inativarFuncionarioUseCase(FuncionarioRepositoryPort funcionarioRepository) {
        return new InativarFuncionarioService(funcionarioRepository);
    }
}
