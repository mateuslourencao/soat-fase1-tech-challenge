package com.oficina.manutencao.domain.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MetricaExecucaoTest {

    @Test
    void deveCalcularMediaSomenteParaOrdensFinalizadas() {
        LocalDateTime agora = LocalDateTime.now();
        OrdemDeServico finalizada = ordem(StatusOS.FINALIZADA, agora.minusHours(2), agora);
        OrdemDeServico emExecucao = ordem(StatusOS.EM_EXECUCAO, agora.minusHours(4), agora);

        MetricaExecucao metrica = MetricaExecucao.calcular(List.of(finalizada, emExecucao), 1);

        assertEquals(7_200_000L, metrica.getTempoMs());
        assertEquals(1, metrica.getDiasAvaliados());
    }

    @Test
    void deveRetornarZeroSemOrdensFinalizadasERejeitarPeriodoInvalido() {
        assertEquals(0L, MetricaExecucao.calcular(List.of(), 1).getTempoMs());
        assertThrows(IllegalArgumentException.class, () -> MetricaExecucao.calcular(List.of(), 0));
    }

    private OrdemDeServico ordem(StatusOS status, LocalDateTime criacao, LocalDateTime atualizacao) {
        return OrdemDeServico.builder()
                .id(1).documentoCliente("123").placaVeiculo("ABC1234")
                .descricaoQueixas("Teste").status(status)
                .dataCriacao(criacao).dataAtualizacao(atualizacao).build();
    }
}
