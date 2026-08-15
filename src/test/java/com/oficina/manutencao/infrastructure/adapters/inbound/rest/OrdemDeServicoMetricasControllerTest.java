package com.oficina.manutencao.infrastructure.adapters.inbound.rest;

import com.oficina.manutencao.domain.model.MetricaExecucao;
import com.oficina.manutencao.domain.ports.inbound.CalcularMetricaExecucaoUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class OrdemDeServicoMetricasControllerTest {

    private final CalcularMetricaExecucaoUseCase calcularMetricaExecucao = mock(CalcularMetricaExecucaoUseCase.class);

    private final OrdemDeServicoMetricasController controller = new OrdemDeServicoMetricasController(
         calcularMetricaExecucao
    );

    @Test
    void deveCalcularMetricaExecucao() {
        int dias = 5;
        MetricaExecucao metricaExecucao = mock(MetricaExecucao.class);
        when(calcularMetricaExecucao.calcularMetricaExecucao(dias)).thenReturn(metricaExecucao);

        ResponseEntity<?> response = controller.calcularMetricaExecucao(dias);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

}
