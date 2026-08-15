package com.oficina.manutencao.infrastructure.adapters.inbound.rest;

import com.oficina.manutencao.domain.ports.inbound.*;
import com.oficina.manutencao.infrastructure.adapters.inbound.rest.dto.OrdemDeServicoResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/ordensdeservico")
@Tag(name = "Ordens de Serviço", description = "Gestão de aberturas e acompanhamento de ordens de serviço")
class OrdemDeServicoStatusController {
    private static final Logger logger = LoggerFactory.getLogger(OrdemDeServicoStatusController.class);

    private final IniciarDiagnosticoUseCase iniciarDiagnostico;
    private final EnviarOrcamentoUseCase enviarOrcamento;
    private final AprovarOrcamentoUseCase aprovarOrcamento;
    private final FinalizarReparoUseCase finalizarReparo;
    private final EntregarVeiculoUseCase entregarVeiculo;

    OrdemDeServicoStatusController(IniciarDiagnosticoUseCase iniciarDiagnostico,
                             EnviarOrcamentoUseCase enviarOrcamento,
                             AprovarOrcamentoUseCase aprovarOrcamento,
                             FinalizarReparoUseCase finalizarReparo,
                             EntregarVeiculoUseCase entregarVeiculo) {
        this.iniciarDiagnostico = iniciarDiagnostico;
        this.enviarOrcamento = enviarOrcamento;
        this.aprovarOrcamento = aprovarOrcamento;
        this.finalizarReparo = finalizarReparo;
        this.entregarVeiculo = entregarVeiculo;
    }

    @PatchMapping("/{id}/iniciar-diagnostico")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Iniciar diagnóstico", description = "Altera o status da ordem de serviço para Em Diagnóstico")
    public ResponseEntity<Void> iniciarDiagnostico(@PathVariable int id) {
        iniciarDiagnostico.iniciarDiagnostico(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/enviar-orcamento")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Enviar orçamento", description = "Altera o status da ordem de serviço para Aguardando Aprovação e notifica o cliente")
    public ResponseEntity<OrdemDeServicoResponseDTO> enviarOrcamento(@PathVariable int id) {
        return ResponseEntity.ok(new OrdemDeServicoResponseDTO( enviarOrcamento.enviarOrcamento(id)));
    }

    @PatchMapping("/{id}/aprovar-orcamento")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Aprovar orçamento", description = "Altera o status da ordem de serviço para Em Execução")
    public ResponseEntity<Void> aprovarOrcamento(@PathVariable int id) {
        aprovarOrcamento.aprovarOrcamento(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/finalizar-reparo")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Finalizar reparo", description = "Altera o status da ordem de serviço para Finalizada")
    public ResponseEntity<Void> finalizarReparo(@PathVariable int id) {
        finalizarReparo.finalizarReparo(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/entregar-veiculo")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Entregar veículo", description = "Altera o status da ordem de serviço para Entregue")
    public ResponseEntity<Void> entregarVeiculo(@PathVariable int id) {
        entregarVeiculo.entregarVeiculo(id);
        return ResponseEntity.noContent().build();
    }
}
