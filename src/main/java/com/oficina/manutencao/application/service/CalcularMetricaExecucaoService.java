package com.oficina.manutencao.application.service;

import com.oficina.manutencao.domain.model.MetricaExecucao;
import com.oficina.manutencao.domain.model.OrdemDeServico;
import com.oficina.manutencao.domain.ports.inbound.CalcularMetricaExecucaoUseCase;
import com.oficina.manutencao.domain.ports.outbound.OrdemDeServicoRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
public class CalcularMetricaExecucaoService implements CalcularMetricaExecucaoUseCase {

    private final OrdemDeServicoRepositoryPort ordemDeServicoRepository;

    public CalcularMetricaExecucaoService(OrdemDeServicoRepositoryPort ordemDeServicoRepositoryPort) {
        this.ordemDeServicoRepository = ordemDeServicoRepositoryPort;
    }

    @Override
    public MetricaExecucao calcularMetricaExecucao(int diasAvaliados) {
        LocalDateTime dataInicio = LocalDateTime.now(ZoneId.of("UTC")).minusDays(diasAvaliados);
        LocalDateTime dataFim = LocalDateTime.now(ZoneId.of("UTC"));
        
        List<OrdemDeServico> oss = ordemDeServicoRepository.buscarOrdensdeServicoPeriodo(dataInicio, dataFim);
        
        return MetricaExecucao.calcular(oss, diasAvaliados);
    }
}
