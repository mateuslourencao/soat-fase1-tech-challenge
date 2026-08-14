package com.oficina.estoque.infrastructure.config;

import com.oficina.estoque.application.service.PecaService;
import com.oficina.estoque.domain.ports.outbound.PecaRepositoryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class PecaConfig {
    @Bean
    PecaService pecaService(PecaRepositoryPort pecaRepository) {
        return new PecaService(pecaRepository);
    }
}
