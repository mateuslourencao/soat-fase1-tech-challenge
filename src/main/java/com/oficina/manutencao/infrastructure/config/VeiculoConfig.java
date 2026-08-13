package com.oficina.manutencao.infrastructure.config;

import com.oficina.manutencao.application.service.*;
import com.oficina.manutencao.domain.ports.inbound.*;
import com.oficina.manutencao.domain.ports.outbound.VeiculoRepositoryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VeiculoConfig {
    @Bean
    public CadastrarVeiculoUseCase cadastrarVeiculoUseCase(VeiculoRepositoryPort repositorio) {
        return new CadastrarVeiculoService(repositorio);
    }

    @Bean
    public AtualizarVeiculoUseCase atualizarVeiculoUseCase(VeiculoRepositoryPort repositorio) {
        return new AtualizarVeiculoService(repositorio);
    }

    @Bean
    public BuscarVeiculoUseCase buscarVeiculoUseCase(VeiculoRepositoryPort repositorio) {
        return new BuscarVeiculoService(repositorio);
    }

    @Bean
    public ListarVeiculosUseCase listarVeiculosUseCase(VeiculoRepositoryPort repositorio) {
        return new ListarVeiculosService(repositorio);
    }

    @Bean
    public RemoverVeiculoUseCase removerVeiculoUseCase(VeiculoRepositoryPort repositorio) {
        return new RemoverVeiculoService(repositorio);
    }
}
