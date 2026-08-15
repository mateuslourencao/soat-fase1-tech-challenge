package com.oficina.manutencao.infrastructure.config;

import com.oficina.estoque.domain.ports.outbound.PecaRepositoryPort;
import com.oficina.estoque.domain.ports.outbound.ServicoRepositoryPort;
import com.oficina.manutencao.application.service.*;
import com.oficina.manutencao.domain.ports.inbound.*;
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
    AtualizarItensOrdemDeServicoUseCase atualizarItensOrdemDeServicoUseCase(
            OrdemDeServicoRepositoryPort repositorio,
            PecaRepositoryPort pecaRepositorio,
            ServicoRepositoryPort servicoRepositorio) {
        return new AtualizarItensOrdemDeServicoService(repositorio, pecaRepositorio, servicoRepositorio);
    }

    @Bean
    IniciarDiagnosticoUseCase iniciarDiagnosticoUseCase(OrdemDeServicoRepositoryPort repositorio) {
        return new IniciarDiagnosticoService(repositorio);
    }

    @Bean
    EnviarOrcamentoUseCase enviarOrcamentoUseCase(OrdemDeServicoRepositoryPort repositorio) {
        return new EnviarOrcamentoService(repositorio);
    }

    @Bean
    AprovarOrcamentoUseCase aprovarOrcamentoUseCase(OrdemDeServicoRepositoryPort repositorio) {
        return new AprovarOrcamentoService(repositorio);
    }

    @Bean
    FinalizarReparoUseCase finalizarReparoUseCase(OrdemDeServicoRepositoryPort repositorio) {
        return new FinalizarReparoService(repositorio);
    }

    @Bean
    EntregarVeiculoUseCase entregarVeiculoUseCase(OrdemDeServicoRepositoryPort repositorio) {
        return new EntregarVeiculoService(repositorio);
    }
}
