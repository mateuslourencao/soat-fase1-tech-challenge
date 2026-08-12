package com.oficina.manutencao.infrastructure.config;

import com.oficina.manutencao.application.service.CriarOrdemDeServicoService;
import com.oficina.manutencao.application.service.ListarOrdemDeServicoService;
import com.oficina.manutencao.domain.ports.inbound.CriarOrdemDeServicoUseCase;
import com.oficina.manutencao.domain.ports.inbound.ListarOrdemDeServicoUseCase;
import com.oficina.manutencao.domain.ports.outbound.OrdemDeServicoRepositoryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class OrdemDeServicoConfig {
    @Bean
    CriarOrdemDeServicoUseCase criarOrdemDeServico(OrdemDeServicoRepositoryPort repositorio) {
        return new CriarOrdemDeServicoService(repositorio);
    }

    @Bean
    ListarOrdemDeServicoUseCase listarOrdemDeServico(OrdemDeServicoRepositoryPort repositorio) {
        return new ListarOrdemDeServicoService(repositorio);
    }

}
