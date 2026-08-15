package com.oficina.manutencao.infrastructure.adapters.inbound.rest;

import com.oficina.manutencao.domain.model.Veiculo;
import com.oficina.manutencao.domain.ports.inbound.*;
import com.oficina.manutencao.infrastructure.adapters.inbound.rest.dto.VeiculoRequestDTO;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class VeiculoControllerTest {

    private final CadastrarVeiculoUseCase cadastrarUseCase = mock(CadastrarVeiculoUseCase.class);
    private final AtualizarVeiculoUseCase atualizarUseCase = mock(AtualizarVeiculoUseCase.class);
    private final BuscarVeiculoUseCase buscarUseCase = mock(BuscarVeiculoUseCase.class);
    private final ListarVeiculosUseCase listarUseCase = mock(ListarVeiculosUseCase.class);
    private final RemoverVeiculoUseCase removerUseCase = mock(RemoverVeiculoUseCase.class);

    private final VeiculoController controller = new VeiculoController(
            cadastrarUseCase, atualizarUseCase, buscarUseCase, listarUseCase, removerUseCase
    );

    @Test
    void deveCriarVeiculoComSucesso() {
        VeiculoRequestDTO request = new VeiculoRequestDTO("ABC1234", "Ford", "Fiesta", 2020);
        Veiculo veiculo = new Veiculo("ABC1234", "Ford", "Fiesta", 2020);
        when(cadastrarUseCase.cadastrarVeiculo(any(Veiculo.class))).thenReturn(veiculo);

        ResponseEntity<?> response = controller.criar(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(cadastrarUseCase).cadastrarVeiculo(any(Veiculo.class));
    }

    @Test
    void deveListarVeiculos() {
        Veiculo veiculo = new Veiculo("ABC1234", "Ford", "Fiesta", 2020);
        when(listarUseCase.listarVeiculos()).thenReturn(List.of(veiculo));

        ResponseEntity<?> response = controller.listar();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(listarUseCase).listarVeiculos();
    }

    @Test
    void deveBuscarVeiculoPorPlaca() {
        String placa = "ABC1234";
        Veiculo veiculo = new Veiculo(placa, "Ford", "Fiesta", 2020);
        when(buscarUseCase.buscarVeiculo(placa)).thenReturn(veiculo);

        ResponseEntity<?> response = controller.buscarPorId(placa);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(buscarUseCase).buscarVeiculo(placa);
    }

    @Test
    void deveAtualizarVeiculo() {
        String placa = "ABC1234";
        VeiculoRequestDTO request = new VeiculoRequestDTO(placa, "Ford", "Focus", 2021);
        Veiculo veiculo = new Veiculo(placa, "Ford", "Focus", 2021);
        when(atualizarUseCase.atualizarVeiculo(eq(placa), any(Veiculo.class))).thenReturn(veiculo);

        ResponseEntity<?> response = controller.atualizar(placa, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(atualizarUseCase).atualizarVeiculo(eq(placa), any(Veiculo.class));
    }

    @Test
    void deveRemoverVeiculo() {
        String placa = "ABC1234";
        doNothing().when(removerUseCase).removerVeiculo(placa);

        ResponseEntity<?> response = controller.remover(placa);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(removerUseCase).removerVeiculo(placa);
    }
}
