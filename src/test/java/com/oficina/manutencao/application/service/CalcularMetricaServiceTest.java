package com.oficina.manutencao.application.service;

import com.oficina.manutencao.domain.model.MetricaExecucao;
import com.oficina.manutencao.domain.model.OrdemDeServico;
import com.oficina.manutencao.domain.model.StatusOS;
import com.oficina.manutencao.domain.ports.outbound.OrdemDeServicoRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CalcularMetricaServiceTest {
    @Mock
    private OrdemDeServicoRepositoryPort repository;

    @Test void deveCalcularMetricaExecucaoComOrdensFinalizadas() {
        LocalDateTime agora = LocalDateTime.now();
        LocalDateTime ontemOito = agora.minusDays(1).withHour(8).withMinute(0).withSecond(0);
        LocalDateTime ontemDez = agora.minusDays(1).withHour(12).withMinute(0).withSecond(0);
        
        OrdemDeServico ordem1 = ordem(StatusOS.FINALIZADA, ontemOito, ontemDez);
        OrdemDeServico ordem2 = ordem(StatusOS.FINALIZADA, ontemOito.plusHours(1), ontemDez.plusHours(1));
        
        when(repository.buscarOrdensdeServicoPeriodo(any(), any())).thenReturn(List.of(ordem1, ordem2));
        
        MetricaExecucao resultado = new CalcularMetricaExecucaoService(repository).calcularMetricaExecucao(1);
        
        assertNotNull(resultado);
        assertTrue(resultado.getTempoMs() > 0);
    }

    @Test void deveRetornarMetricaZeroQuandoNaoHouverOrdensFinalizadas() {
        when(repository.buscarOrdensdeServicoPeriodo(any(), any())).thenReturn(List.of());
        
        MetricaExecucao resultado = new CalcularMetricaExecucaoService(repository).calcularMetricaExecucao(1);
        
        assertNotNull(resultado);
        assertEquals(0L, resultado.getTempoMs());
    }

    @Test void deveIgnorarOrdensNaoFinalizadas() {
        LocalDateTime agora = LocalDateTime.now();
        LocalDateTime inicio = agora.minusHours(1);
        
        OrdemDeServico ordem1 = ordem(StatusOS.FINALIZADA, inicio, agora);
        OrdemDeServico ordem2 = ordem(StatusOS.EM_DIAGNOSTICO, inicio, agora);
        OrdemDeServico ordem3 = ordem(StatusOS.EM_EXECUCAO, inicio, agora);
        Long ResultadoEsperado = Duration.between(inicio, agora).toMillis();

        when(repository.buscarOrdensdeServicoPeriodo(any(), any())).thenReturn(List.of(ordem1, ordem2, ordem3));
        
        MetricaExecucao resultado = new CalcularMetricaExecucaoService(repository).calcularMetricaExecucao(1);

        assertEquals(resultado.getTempoMs(), ResultadoEsperado);
    }

    @Test void deveCalcularMediaCorretaParaMultiplasOrdensFinalizadas() {
        LocalDateTime agora = LocalDateTime.now();
        
        OrdemDeServico ordem1 = ordem(StatusOS.FINALIZADA, agora.minusHours(2), agora.minusHours(0));
        OrdemDeServico ordem2 = ordem(StatusOS.FINALIZADA, agora.minusHours(4), agora.minusHours(2));
        
        when(repository.buscarOrdensdeServicoPeriodo(any(), any())).thenReturn(List.of(ordem1, ordem2));
        
        MetricaExecucao resultado = new CalcularMetricaExecucaoService(repository).calcularMetricaExecucao(1);

        var diferenca1 = Duration.between(agora.minusHours(2), agora.minusHours(0));
        var diferenca2 = Duration.between(agora.minusHours(4), agora.minusHours(2));
        long duracaoEsperada = (diferenca1.toMillis()+diferenca2.toMillis())/2;
        assertEquals(duracaoEsperada, resultado.getTempoMs());
    }

    private OrdemDeServico ordem(StatusOS status, LocalDateTime dataCriacao, LocalDateTime dataAtualizacao) {
        return new OrdemDeServico(1, "123", "ABC1234", List.of(), List.of(), 0, status, dataCriacao, dataAtualizacao, "Teste", null);
    }
}
