package com.oficina.manutencao.infrastructure.config;

import com.oficina.manutencao.application.service.AtualizarItensOrdemDeServicoService;
import com.oficina.manutencao.application.service.BuscarOrdemDeServicoService;
import com.oficina.manutencao.application.service.CadastrarOrdemDeServicoService;
import com.oficina.manutencao.application.service.ListarOrdensDeServicoService;
import com.oficina.manutencao.domain.ports.inbound.AtualizarItensOrdemDeServicoUseCase;
import com.oficina.manutencao.domain.ports.inbound.BuscarOrdemDeServicoUseCase;
import com.oficina.manutencao.domain.ports.inbound.CadastrarOrdemDeServicoUseCase;
import com.oficina.manutencao.domain.ports.inbound.ListarOrdensDeServicoUseCase;
import com.oficina.manutencao.domain.ports.outbound.OrdemDeServicoRepositoryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class OrdemDeServicoConfig {
    @Bean
    CadastrarOrdemDeServicoUseCase criarOrdemDeServico(OrdemDeServicoRepositoryPort repositorio) {
        return new CadastrarOrdemDeServicoService(repositorio);
    }

    @Bean
    ListarOrdensDeServicoUseCase listarOrdemDeServico(OrdemDeServicoRepositoryPort repositorio) {
        return new ListarOrdensDeServicoService(repositorio);
    }

    @Bean
    BuscarOrdemDeServicoUseCase buscarOrdemDeServico(OrdemDeServicoRepositoryPort repositorio) {
        return new BuscarOrdemDeServicoService(repositorio);
    }

    @Bean
    AtualizarItensOrdemDeServicoUseCase atualizarItensOrdemDeServicoUseCase(OrdemDeServicoRepositoryPort repositorio) {
        return new AtualizarItensOrdemDeServicoService(repositorio);
    }
}
