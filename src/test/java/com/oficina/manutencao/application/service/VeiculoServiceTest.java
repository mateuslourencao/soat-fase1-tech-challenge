package com.oficina.manutencao.application.service;

import com.oficina.manutencao.domain.model.Veiculo;
import com.oficina.manutencao.domain.ports.outbound.VeiculoRepositoryPort;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VeiculoServiceTest {
    private final VeiculoRepositoryPort repository = mock(VeiculoRepositoryPort.class);

    @Test void deveCadastrarVeiculo() {
        Veiculo veiculo = new Veiculo("ABC1234", "Toyota", "Corolla", 2022);
        when(repository.salvar(veiculo)).thenReturn(veiculo);
        
        Veiculo resultado = new CadastrarVeiculoService(repository).cadastrarVeiculo(veiculo);
        
        assertSame(veiculo, resultado);
        verify(repository).salvar(veiculo);
    }

    @Test void deveBuscarVeiculo() {
        Veiculo veiculo = new Veiculo("ABC1234", "Toyota", "Corolla", 2022);
        when(repository.buscarPorId("ABC1234")).thenReturn(Optional.of(veiculo));
        
        Veiculo resultado = new BuscarVeiculoService(repository).buscarVeiculo("ABC1234");
        
        assertSame(veiculo, resultado);
    }

    @Test void deveLancarExcecaoQuandoVeiculoNaoEncontrado() {
        when(repository.buscarPorId("XYZ9999")).thenReturn(Optional.empty());
        
        BuscarVeiculoService service = new BuscarVeiculoService(repository);
        assertThrows(RuntimeException.class, () -> service.buscarVeiculo("XYZ9999"));
    }

    @Test void deveListarVeiculos() {
        List<Veiculo> veiculos = List.of(new Veiculo("ABC1234", "Toyota", "Corolla", 2022));
        when(repository.listarTodos()).thenReturn(veiculos);
        
        List<Veiculo> resultado = new ListarVeiculosService(repository).listarVeiculos();
        
        assertSame(veiculos, resultado);
    }

    @Test void deveAtualizarVeiculo() {
        Veiculo existente = new Veiculo("ABC1234", "Toyota", "Corolla", 2020);
        Veiculo atualizado = new Veiculo("ABC1234", "Toyota", "Corolla Cross", 2022);
        
        when(repository.buscarPorId("ABC1234")).thenReturn(Optional.of(existente));
        when(repository.salvar(any(Veiculo.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        Veiculo resultado = new AtualizarVeiculoService(repository).atualizarVeiculo("ABC1234", atualizado);
        
        assertEquals("Corolla Cross", resultado.getModelo());
        assertEquals(2022, resultado.getAno());
        verify(repository).salvar(any(Veiculo.class));
    }

    @Test void deveRemoverVeiculo() {
        new RemoverVeiculoService(repository).removerVeiculo("ABC1234");
        verify(repository).remover("ABC1234");
    }
}
