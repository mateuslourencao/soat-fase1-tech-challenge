package com.oficina.manutencao.infrastructure.adapters.inbound.rest;

import com.oficina.manutencao.domain.model.OrdemDeServico;
import com.oficina.manutencao.domain.ports.inbound.*;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class OrdemDeServicoStatusControllerTest {

     private final IniciarDiagnosticoUseCase iniciarDiagnosticoUseCase = mock(IniciarDiagnosticoUseCase.class);
    private final EnviarOrcamentoUseCase enviarOrcamentoUseCase = mock(EnviarOrcamentoUseCase.class);
    private final AprovarOrcamentoUseCase aprovarOrcamentoUseCase = mock(AprovarOrcamentoUseCase.class);
    private final FinalizarReparoUseCase finalizarReparoUseCase = mock(FinalizarReparoUseCase.class);
    private final EntregarVeiculoUseCase entregarVeiculoUseCase = mock(EntregarVeiculoUseCase.class);

    private final OrdemDeServicoStatusController controller = new OrdemDeServicoStatusController(
            iniciarDiagnosticoUseCase, enviarOrcamentoUseCase, aprovarOrcamentoUseCase,
            finalizarReparoUseCase, entregarVeiculoUseCase
    );

    @Test
    void deveIniciarDiagnostico() {
        int id = 1;
        ResponseEntity<Void> response = controller.iniciarDiagnostico(id);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(iniciarDiagnosticoUseCase).iniciarDiagnostico(id);
    }

    @Test
    void deveEnviarOrcamento() {
        int id = 1;
        OrdemDeServico ordemDeServico = OrdemDeServico.builder()
                .id(id)
                .documentoCliente("12345678901")
                .placaVeiculo("ABC1234")
                .descricaoQueixas("Problema no motor")
                .build();
        when(enviarOrcamentoUseCase.enviarOrcamento(id)).thenReturn(ordemDeServico);
        ResponseEntity<?> response = controller.enviarOrcamento(id);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(enviarOrcamentoUseCase).enviarOrcamento(id);
    }

    @Test
    void deveAprovarOrcamento() {
        int id = 1;
        ResponseEntity<Void> response = controller.aprovarOrcamento(id);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(aprovarOrcamentoUseCase).aprovarOrcamento(id);
    }

    @Test
    void deveFinalizarReparo() {
        int id = 1;
        ResponseEntity<Void> response = controller.finalizarReparo(id);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(finalizarReparoUseCase).finalizarReparo(id);
    }

    @Test
    void deveEntregarVeiculo() {
        int id = 1;
        ResponseEntity<Void> response = controller.entregarVeiculo(id);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(entregarVeiculoUseCase).entregarVeiculo(id);
    }
}
