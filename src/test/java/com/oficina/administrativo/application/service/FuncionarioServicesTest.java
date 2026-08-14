package com.oficina.administrativo.application.service;

import com.oficina.administrativo.domain.model.Funcionario;
import com.oficina.administrativo.domain.model.PerfilFuncionario;
import com.oficina.administrativo.domain.ports.outbound.FuncionarioRepositoryPort;
import com.oficina.administrativo.domain.ports.outbound.SenhaCriptografadaPort;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class FuncionarioServicesTest {
    @Test
    void deveCadastrarFuncionarioComSenhaCriptografada() {
        FuncionarioRepositoryPort repository = mock(FuncionarioRepositoryPort.class);
        SenhaCriptografadaPort senha = mock(SenhaCriptografadaPort.class);
        Funcionario funcionario = new Funcionario(0, "Ana", " ANA@OFICINA.COM ", null, PerfilFuncionario.MECANICO, false);
        when(repository.buscarPorEmail("ana@oficina.com")).thenReturn(Optional.empty());
        when(senha.criptografar("segredo")).thenReturn("hash");
        when(repository.salvar(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Funcionario salvo = new CadastrarFuncionarioService(repository, senha).cadastrarFuncionario(funcionario, "segredo");

        assertAll(() -> assertEquals("Ana", salvo.getNome()), () -> assertEquals("ana@oficina.com", salvo.getEmail()),
                () -> assertEquals("hash", salvo.getSenhaHash()), () -> assertTrue(salvo.isAtivo()));
    }

    @Test
    void deveListarBuscarAtualizarEAlterarStatus() {
        FuncionarioRepositoryPort repository = mock(FuncionarioRepositoryPort.class);
        Funcionario existente = new Funcionario(1, "Ana", "ana@oficina.com", "hash", PerfilFuncionario.MECANICO, true);
        when(repository.buscarPorId(1)).thenReturn(Optional.of(existente));
        when(repository.buscarPorEmail("novo@oficina.com")).thenReturn(Optional.empty());
        when(repository.listarTodos()).thenReturn(List.of(existente));
        when(repository.salvar(any())).thenAnswer(invocation -> invocation.getArgument(0));

        assertSame(existente, new BuscarFuncionarioService(repository).buscarFuncionario(1));
        assertEquals(List.of(existente), new ListarFuncionariosService(repository).listarFuncionarios());
        Funcionario atualizado = new AtualizarFuncionarioService(repository)
                .atualizarFuncionario(1, new Funcionario(1, "Ana Silva", "novo@oficina.com", null, PerfilFuncionario.ADMIN, false));
        assertAll(() -> assertEquals("Ana Silva", atualizado.getNome()), () -> assertEquals("hash", atualizado.getSenhaHash()),
                () -> assertTrue(atualizado.isAtivo()));

        AtivarFuncionarioService ativarService = new AtivarFuncionarioService(repository);
        InativarFuncionarioService inativarService = new InativarFuncionarioService(repository);
        inativarService.inativarFuncionario(1);
        verify(repository, atLeastOnce()).salvar(argThat(funcionario -> !funcionario.isAtivo()));
        ativarService.ativarFuncionario(1);
        verify(repository, atLeastOnce()).salvar(argThat(Funcionario::isAtivo));
    }
}
