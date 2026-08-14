package com.oficina.manutencao.domain.model;

public class MetricaExecucao {
    private final long tempoMs;
    private final int diasAvaliados;

    public MetricaExecucao(long tempoMs, int diasAvaliados) {
        this.tempoMs = tempoMs;
        this.diasAvaliados = diasAvaliados;
    }

    public long getTempoMs() {
        return tempoMs;
    }

    public int getDiasAvaliados() {
        return diasAvaliados;
    }
}
