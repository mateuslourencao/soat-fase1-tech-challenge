package com.oficina.estoque.infrastructure.config;

import com.oficina.estoque.application.service.PecaService;
import com.oficina.estoque.domain.ports.outbound.PecaRepositoryPort;
import com.oficina.estoque.domain.ports.outbound.ServicoRepositoryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class EstoqueConfig {
    @Bean
    PecaService estoqueService(PecaRepositoryPort pecaRepository, ServicoRepositoryPort servicoRepository) {
        return new PecaService(pecaRepository, servicoRepository);
    }
}
