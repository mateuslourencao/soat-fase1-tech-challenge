package com.oficina.manutencao.infrastructure.adapters.inbound.rest;

import com.oficina.manutencao.domain.model.OrdemDeServico;
import com.oficina.manutencao.domain.ports.inbound.BuscarOrdemDeServicoUseCase;
import com.oficina.manutencao.domain.ports.inbound.AprovarOrcamentoClienteUseCase;
import com.oficina.manutencao.infrastructure.adapters.inbound.rest.dto.AprovarOrcamentoPublicoRequestDTO;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class OrdemDeServicoPublicoControllerTest {
    private final BuscarOrdemDeServicoUseCase buscar = mock(BuscarOrdemDeServicoUseCase.class);
    private final AprovarOrcamentoClienteUseCase aprovarOrcamentoCliente = mock(AprovarOrcamentoClienteUseCase.class);
    private final OrdemDeServicoPublicoController controller = new OrdemDeServicoPublicoController(buscar, aprovarOrcamentoCliente);

    @Test
    void deveAprovarOrcamento() {
        var response = controller.aprovarOrcamento(1,
                new AprovarOrcamentoPublicoRequestDTO("12345678900", true));

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(aprovarOrcamentoCliente).aprovarOrcamento(1, "12345678900", true);
    }

    @Test
    void deveRejeitarOrcamento() {
        var response = controller.aprovarOrcamento(1,
                new AprovarOrcamentoPublicoRequestDTO("12345678900", false));

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(aprovarOrcamentoCliente).aprovarOrcamento(1, "12345678900", false);
    }

    @Test
    void deveBuscarOrdem() {
        OrdemDeServico ordem = OrdemDeServico.builder()
                .id(1)
                .documentoCliente("12345678900")
                .placaVeiculo("ABC1234")
                .descricaoQueixas("Problema")
                .build();
        when(buscar.buscarOrdemDeServico(1)).thenReturn(ordem);

        var response = controller.buscarPorId(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(buscar).buscarOrdemDeServico(1);
    }
}
