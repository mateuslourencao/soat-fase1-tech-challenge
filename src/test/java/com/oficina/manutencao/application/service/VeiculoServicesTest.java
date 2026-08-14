package com.oficina.manutencao.application.service;

import com.oficina.manutencao.domain.model.Veiculo;
import com.oficina.manutencao.domain.ports.outbound.VeiculoRepositoryPort;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

class VeiculoServicesTest {
    private final VeiculoRepositoryPort repository = mock(VeiculoRepositoryPort.class);
    private final Veiculo veiculo = new Veiculo("ABC1234", "Ford", "Ka", 2020);

    @Test void deveCadastrarVeiculo() { when(repository.salvar(veiculo)).thenReturn(veiculo); assertSame(veiculo, new CadastrarVeiculoService(repository).cadastrarVeiculo(veiculo)); }
    @Test void deveBuscarVeiculo() { when(repository.buscarPorId("ABC1234")).thenReturn(Optional.of(veiculo)); assertSame(veiculo, new BuscarVeiculoService(repository).buscarVeiculo("ABC1234")); }
    @Test void deveListarVeiculos() { List<Veiculo> veiculos = List.of(veiculo); when(repository.listarTodos()).thenReturn(veiculos); assertSame(veiculos, new ListarVeiculosService(repository).listarVeiculos()); }

    @Test void deveAtualizarVeiculoMantendoPlacaExistente() {
        when(repository.buscarPorId("ABC1234")).thenReturn(Optional.of(veiculo));
        when(repository.salvar(any(Veiculo.class))).thenAnswer(invocation -> invocation.getArgument(0));
        Veiculo resultado = new AtualizarVeiculoService(repository).atualizarVeiculo("ABC1234", new Veiculo("NOVA", "Honda", "Civic", 2024));
        assertEquals("ABC1234", resultado.getPlaca());
        assertEquals("Honda", resultado.getMarca());
    }

    @Test void deveRemoverVeiculo() { when(repository.buscarPorId("ABC1234")).thenReturn(Optional.of(veiculo)); new RemoverVeiculoService(repository).removerVeiculo("ABC1234"); verify(repository).remover("ABC1234"); }
}
