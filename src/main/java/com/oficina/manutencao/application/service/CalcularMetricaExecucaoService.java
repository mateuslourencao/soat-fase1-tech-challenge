package com.oficina.manutencao.application.service;

import com.oficina.manutencao.domain.model.MetricaExecucao;
import com.oficina.manutencao.domain.model.OrdemDeServico;
import com.oficina.manutencao.domain.model.StatusOS;
import com.oficina.manutencao.domain.ports.inbound.CalcularMetricaExecucaoUseCase;
import com.oficina.manutencao.domain.ports.outbound.OrdemDeServicoRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
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
        
        List<Long> temposMs = oss.stream()
                .filter(os -> os.getStatus() == StatusOS.FINALIZADA)
                .map(os -> Duration.between(
                        os.getDataCriacao().atZone(ZoneOffset.UTC),
                        os.getDataAtualizacao().atZone(ZoneOffset.UTC)
                ).toMillis())
                .toList();

        long tempoMedio = calcularMedia(temposMs);
        
        return new MetricaExecucao(tempoMedio, diasAvaliados);
    }

    private long calcularMedia(List<Long> temposMs) {
        if (temposMs.isEmpty()) {
            return  (long) 0;
        }
        return (long)temposMs.stream()
                .mapToLong(Long::longValue)
                .average()
                .orElse(0.0);
    }
}
