package com.oficina.manutencao.infrastructure.config;

import com.oficina.estoque.domain.ports.outbound.PecaRepositoryPort;
import com.oficina.estoque.domain.ports.outbound.ServicoRepositoryPort;
import com.oficina.manutencao.application.service.*;
import com.oficina.manutencao.domain.ports.inbound.*;
import com.oficina.manutencao.domain.ports.outbound.ClienteRepositoryPort;
import com.oficina.manutencao.domain.ports.outbound.NotificarClientePort;
import com.oficina.manutencao.domain.ports.outbound.OrdemDeServicoRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class OrdemDeServicoConfig {

    private static final Logger logger = LoggerFactory.getLogger(OrdemDeServicoConfig.class);

    @Bean
    NotificarClientePort notificarClientePort() {
        return (cliente, os) -> {
            logger.info("SIMULAÇÃO: Enviando notificação para o cliente {} (Email: {})", cliente.getNome(), cliente.getEmail());
            logger.info("SIMULAÇÃO: Orçamento da OS #{} no valor de R$ {} aguarda sua aprovação.", os.getId(), os.getOrcamento());
            logger.info("SIMULAÇÃO: Link para aprovação: http://oficina.com/aprovar/{}", os.getId());
        };
    }
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
    EnviarOrcamentoUseCase enviarOrcamentoUseCase(OrdemDeServicoRepositoryPort repositorio,
                                                  ClienteRepositoryPort clienteRepositorio,
                                                  NotificarClientePort notificarCliente) {
        return new EnviarOrcamentoService(repositorio, clienteRepositorio, notificarCliente);
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
