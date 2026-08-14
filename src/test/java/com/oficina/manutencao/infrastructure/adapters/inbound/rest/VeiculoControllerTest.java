package com.oficina.manutencao.infrastructure.adapters.inbound.rest;

import com.oficina.manutencao.domain.model.Veiculo;
import com.oficina.manutencao.domain.ports.inbound.*;
import com.oficina.manutencao.infrastructure.adapters.inbound.rest.dto.VeiculoRequestDTO;
import com.oficina.manutencao.infrastructure.adapters.inbound.rest.dto.VeiculoResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class VeiculoControllerTest {
    private final CadastrarVeiculoUseCase cadastrarUseCase = mock(CadastrarVeiculoUseCase.class);
    private final AtualizarVeiculoUseCase atualizarUseCase = mock(AtualizarVeiculoUseCase.class);
    private final BuscarVeiculoUseCase buscarUseCase = mock(BuscarVeiculoUseCase.class);
    private final ListarVeiculosUseCase listarUseCase = mock(ListarVeiculosUseCase.class);
    private final RemoverVeiculoUseCase removerUseCase = mock(RemoverVeiculoUseCase.class);
    private final VeiculoController controller = new VeiculoController(cadastrarUseCase, atualizarUseCase, buscarUseCase, listarUseCase, removerUseCase);

    @Test void deveCriarVeiculo() {
        VeiculoRequestDTO request = new VeiculoRequestDTO("ABC1234", "Fiat", "Uno", 2020);
        Veiculo veiculo = new Veiculo("ABC1234", "Fiat", "Uno", 2020);
        when(cadastrarUseCase.cadastrarVeiculo(any())).thenReturn(veiculo);

        ResponseEntity<VeiculoResponseDTO> response = controller.criar(request);

        assertEquals(201, response.getStatusCode().value());
        assertEquals("ABC1234", response.getBody().placa());
    }

    @Test void deveListarVeiculos() {
        Veiculo veiculo = new Veiculo("ABC1234", "Fiat", "Uno", 2020);
        when(listarUseCase.listarVeiculos()).thenReturn(List.of(veiculo));

        ResponseEntity<List<VeiculoResponseDTO>> response = controller.listar();

        assertEquals(200, response.getStatusCode().value());
        assertEquals(1, response.getBody().size());
    }

    @Test void deveBuscarPorPlaca() {
        Veiculo veiculo = new Veiculo("ABC1234", "Fiat", "Uno", 2020);
        when(buscarUseCase.buscarVeiculo("ABC1234")).thenReturn(veiculo);

        ResponseEntity<VeiculoResponseDTO> response = controller.buscarPorId("ABC1234");

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Uno", response.getBody().modelo());
    }

    @Test void deveAtualizarVeiculo() {
        VeiculoRequestDTO request = new VeiculoRequestDTO("ABC1234", "Fiat", "Uno Turbo", 2020);
        Veiculo veiculo = new Veiculo("ABC1234", "Fiat", "Uno Turbo", 2020);
        when(atualizarUseCase.atualizarVeiculo(eq("ABC1234"), any())).thenReturn(veiculo);

        ResponseEntity<VeiculoResponseDTO> response = controller.atualizar("ABC1234", request);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Uno Turbo", response.getBody().modelo());
    }

    @Test void deveRemoverVeiculo() {
        ResponseEntity<Void> response = controller.remover("ABC1234");
        assertEquals(204, response.getStatusCode().value());
        verify(removerUseCase).removerVeiculo("ABC1234");
    }
}
