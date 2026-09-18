package com.oficina.manutencao.domain.model;

import java.time.Duration;
import java.time.ZoneOffset;
import java.util.List;

public class MetricaExecucao {
    private final long tempoMs;
    private final int diasAvaliados;

    public MetricaExecucao(long tempoMs, int diasAvaliados) {
        if (tempoMs < 0 || diasAvaliados <= 0) {
            throw new IllegalArgumentException("Métrica de execução inválida");
        }
        this.tempoMs = tempoMs;
        this.diasAvaliados = diasAvaliados;
    }

    public static MetricaExecucao calcular(List<OrdemDeServico> ordens, int diasAvaliados) {
        if (diasAvaliados <= 0) {
            throw new IllegalArgumentException("Dias avaliados deve ser positivo");
        }
        long tempoMedio = (long) ordens.stream()
                .filter(ordem -> ordem.getStatus() == StatusOS.FINALIZADA)
                .mapToLong(ordem -> Duration.between(
                        ordem.getDataCriacao().atZone(ZoneOffset.UTC),
                        ordem.getDataAtualizacao().atZone(ZoneOffset.UTC)
                ).toMillis())
                .average()
                .orElse(0.0);
        return new MetricaExecucao(tempoMedio, diasAvaliados);
    }

    public long getTempoMs() {
        return tempoMs;
    }

    public int getDiasAvaliados() {
        return diasAvaliados;
    }
}
