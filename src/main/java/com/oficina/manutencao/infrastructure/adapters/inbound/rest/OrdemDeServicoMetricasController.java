package com.oficina.manutencao.infrastructure.adapters.inbound.rest;

import com.oficina.manutencao.domain.ports.inbound.CalcularMetricaExecucaoUseCase;
import com.oficina.manutencao.infrastructure.adapters.inbound.rest.dto.RelatorioTempoMedioResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/ordensdeservico")
@Tag(name = "Ordens de Serviço", description = "Gestão de aberturas e acompanhamento de ordens de serviço")
class OrdemDeServicoMetricasController {
    private final CalcularMetricaExecucaoUseCase calcularMetricaExecucao;

    OrdemDeServicoMetricasController(CalcularMetricaExecucaoUseCase calcularMetricaExecucao) {
        this.calcularMetricaExecucao = calcularMetricaExecucao;
    }

    @GetMapping("/metricas/{dias}")
    @Operation(summary = "Buscar tempo médio de execução de OS", description = "Buscar tempo médio de execução de OS dos úlimos x dias")
    @ApiResponse(responseCode = "200", description = "Média de tempo de execução calculada")
    @ApiResponse(responseCode = "404", description = "Sem registros de Ordem de Serviço finalizada no período indicado")
    public ResponseEntity<RelatorioTempoMedioResponseDTO> calcularMetricaExecucao(@Parameter(description = "Dias para cálculo") @PathVariable int dias) {

        return ResponseEntity.ok(new RelatorioTempoMedioResponseDTO(dias, calcularMetricaExecucao.calcularMetricaExecucao(dias).getTempoMs()));
    }
}
