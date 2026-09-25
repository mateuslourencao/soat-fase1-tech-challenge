package com.oficina.manutencao.infrastructure.config;

import com.oficina.estoque.domain.ports.outbound.PecaRepositoryPort;
import com.oficina.estoque.domain.ports.outbound.ServicoRepositoryPort;
import com.oficina.manutencao.application.service.*;
import com.oficina.manutencao.domain.ports.inbound.*;
import com.oficina.manutencao.domain.ports.outbound.ClienteRepositoryPort;
import com.oficina.manutencao.domain.ports.outbound.NotificarClientePort;
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
    ListarOrdensDeServicoAbertasUseCase listarOrdensDeServicoAbertas(OrdemDeServicoRepositoryPort repositorio) {
        return new ListarOrdensDeServicoAbertasService(repositorio);
    }

    @Bean
    BuscarOrdemDeServicoUseCase buscarOrdemDeServico(OrdemDeServicoRepositoryPort repositorio) {
        return new BuscarOrdemDeServicoService(repositorio);
    }

    @Bean
    BuscarStatusOrdemDeServicoUseCase buscarStatusOrdemDeServico(OrdemDeServicoRepositoryPort repositorio) {
        return new BuscarStatusOrdemDeServicoService(repositorio);
    }

    @Bean
    AtualizarItensOrdemDeServicoUseCase atualizarItensOrdemDeServicoUseCase(
            OrdemDeServicoRepositoryPort repositorio,
            PecaRepositoryPort pecaRepositorio,
            ServicoRepositoryPort servicoRepositorio) {
        return new AtualizarItensOrdemDeServicoService(repositorio, pecaRepositorio, servicoRepositorio);
    }

    @Bean
    IniciarDiagnosticoUseCase iniciarDiagnosticoUseCase(OrdemDeServicoRepositoryPort repositorio,
                                                        ClienteRepositoryPort clienteRepositorio,
                                                        NotificarClientePort notificarCliente) {
        return new IniciarDiagnosticoService(repositorio, clienteRepositorio, notificarCliente);
    }

    @Bean
    EnviarOrcamentoUseCase enviarOrcamentoUseCase(OrdemDeServicoRepositoryPort repositorio,
                                                  ClienteRepositoryPort clienteRepositorio,
                                                  NotificarClientePort notificarCliente) {
        return new EnviarOrcamentoService(repositorio, clienteRepositorio, notificarCliente);
    }

    @Bean
    AprovarOrcamentoUseCase aprovarOrcamentoUseCase(OrdemDeServicoRepositoryPort repositorio,
                                                    ClienteRepositoryPort clienteRepositorio,
                                                    NotificarClientePort notificarCliente) {
        return new AprovarOrcamentoService(repositorio, clienteRepositorio, notificarCliente);
    }

    @Bean
    AprovarOrcamentoClienteUseCase aprovarOrcamentoClienteUseCase(OrdemDeServicoRepositoryPort repositorio,
                                                                  ClienteRepositoryPort clienteRepositorio,
                                                                  NotificarClientePort notificarCliente) {
        return new AprovarOrcamentoClienteService(repositorio, clienteRepositorio, notificarCliente);
    }

    @Bean
    FinalizarReparoUseCase finalizarReparoUseCase(OrdemDeServicoRepositoryPort repositorio,
                                                  ClienteRepositoryPort clienteRepositorio,
                                                  NotificarClientePort notificarCliente) {
        return new FinalizarReparoService(repositorio, clienteRepositorio, notificarCliente);
    }

    @Bean
    EntregarVeiculoUseCase entregarVeiculoUseCase(OrdemDeServicoRepositoryPort repositorio,
                                                  ClienteRepositoryPort clienteRepositorio,
                                                  NotificarClientePort notificarCliente) {
        return new EntregarVeiculoService(repositorio, clienteRepositorio, notificarCliente);
    }
}
