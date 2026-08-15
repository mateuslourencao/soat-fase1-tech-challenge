package com.oficina.estoque.infrastructure.config;

import com.oficina.estoque.application.service.ServicoService;
import com.oficina.estoque.domain.ports.outbound.ServicoRepositoryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class ServicoConfig {
    @Bean
    ServicoService servicoService(ServicoRepositoryPort servicoRepository) {
        return new ServicoService(servicoRepository);
    }
}
