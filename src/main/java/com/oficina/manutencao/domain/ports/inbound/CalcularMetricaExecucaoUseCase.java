package com.oficina.manutencao.domain.ports.inbound;

import com.oficina.manutencao.domain.model.MetricaExecucao;

public interface CalcularMetricaExecucaoUseCase {
    MetricaExecucao calcularMetricaExecucao(int diasAvaliados);
}
