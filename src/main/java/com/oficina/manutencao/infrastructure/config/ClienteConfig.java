package com.oficina.manutencao.infrastructure.config;

import com.oficina.manutencao.application.service.*;
import com.oficina.manutencao.domain.ports.inbound.*;
import com.oficina.manutencao.domain.ports.outbound.ClienteRepositoryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClienteConfig {

    @Bean
    public CadastrarClienteUseCase cadastrarClienteUseCase(ClienteRepositoryPort repositorio) {
        return new CadastrarClienteService(repositorio);
    }

    @Bean
    public AtualizarClienteUseCase atualizarClienteUseCase(ClienteRepositoryPort repositorio) {
        return new AtualizarClienteService(repositorio);
    }

    @Bean
    public BuscarClienteUseCase buscarClienteUseCase(ClienteRepositoryPort repositorio) {
        return new BuscarClienteService(repositorio);
    }

    @Bean
    public ListarClientesUseCase listarClientesUseCase(ClienteRepositoryPort repositorio) {
        return new ListarClientesService(repositorio);
    }

    @Bean
    public RemoverClienteUseCase deletarClienteUseCase(ClienteRepositoryPort repositorio) {
        return new RemoverClienteService(repositorio);
    }
}