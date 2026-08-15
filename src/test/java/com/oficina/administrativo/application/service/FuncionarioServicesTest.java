package com.oficina.administrativo.application.service;

import com.oficina.common.domain.exception.EntidadeNaoEncontradaException;
import com.oficina.administrativo.domain.model.Funcionario;
import com.oficina.administrativo.domain.model.PerfilFuncionario;
import com.oficina.administrativo.domain.ports.outbound.FuncionarioRepositoryPort;
import com.oficina.administrativo.domain.ports.outbound.SenhaCriptografadaPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FuncionarioServicesTest {

    @Mock
    private FuncionarioRepositoryPort repository;

    @Mock
    private SenhaCriptografadaPort senhaPort;

    @Test
    void deveCadastrarFuncionarioComSucesso() {
        Funcionario funcionario = new Funcionario(0, "Ana", " ANA@OFICINA.COM ", null, PerfilFuncionario.MECANICO, false);
        
        when(repository.buscarPorEmail("ana@oficina.com")).thenReturn(Optional.empty());
        when(senhaPort.criptografar("segredo")).thenReturn("hash");
        when(repository.salvar(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Funcionario salvo = new CadastrarFuncionarioService(repository, senhaPort).cadastrarFuncionario(funcionario, "segredo");

        assertAll(
            () -> assertEquals("Ana", salvo.getNome()),
            () -> assertEquals("ana@oficina.com", salvo.getEmail()),
            () -> assertEquals("hash", salvo.getSenhaHash()),
            () -> assertTrue(salvo.isAtivo())
        );
    }

    @Test
    void deveLancarExcecaoAoCadastrarComDadosInvalidos() {
        CadastrarFuncionarioService service = new CadastrarFuncionarioService(repository, senhaPort);

        assertThrows(IllegalArgumentException.class, () -> service.cadastrarFuncionario(null, "senha"));
        assertThrows(IllegalArgumentException.class, () -> service.cadastrarFuncionario(new Funcionario(0, "", "email@email.com", null, PerfilFuncionario.MECANICO, true), "senha"));
        assertThrows(IllegalArgumentException.class, () -> service.cadastrarFuncionario(new Funcionario(0, "Nome", "", null, PerfilFuncionario.MECANICO, true), "senha"));
        assertThrows(IllegalArgumentException.class, () -> service.cadastrarFuncionario(new Funcionario(0, "Nome", "email@email.com", null, null, true), "senha"));
        assertThrows(IllegalArgumentException.class, () -> service.cadastrarFuncionario(new Funcionario(0, "Nome", "email@email.com", null, PerfilFuncionario.MECANICO, true), ""));
    }

    @Test
    void deveLancarExcecaoAoCadastrarEmailDuplicado() {
        Funcionario funcionario = new Funcionario(0, "Ana", "ana@oficina.com", null, PerfilFuncionario.MECANICO, true);
        
        when(repository.buscarPorEmail("ana@oficina.com")).thenReturn(Optional.of(funcionario));
        CadastrarFuncionarioService service = new CadastrarFuncionarioService(repository, senhaPort);

        assertThrows(IllegalArgumentException.class, () -> service.cadastrarFuncionario(funcionario, "senha"));
    }

    @Test
    void deveBuscarFuncionarioComSucesso() {
        Funcionario existente = new Funcionario(1, "Ana", "ana@oficina.com", "hash", PerfilFuncionario.MECANICO, true);
        when(repository.buscarPorId(1)).thenReturn(Optional.of(existente));

        Funcionario buscado = new BuscarFuncionarioService(repository).buscarFuncionario(1);

        assertSame(existente, buscado);
    }

    @Test
    void deveListarFuncionarios() {
        List<Funcionario> lista = List.of(new Funcionario(1, "Ana", "ana@oficina.com", "hash", PerfilFuncionario.MECANICO, true));
        when(repository.listarTodos()).thenReturn(lista);

        List<Funcionario> resultado = new ListarFuncionariosService(repository).listarFuncionarios();

        assertEquals(lista, resultado);
    }

    @Test
    void deveAtualizarFuncionarioComSucesso() {
        Funcionario existente = new Funcionario(1, "Ana", "ana@oficina.com", "hash", PerfilFuncionario.MECANICO, true);
        Funcionario novosDados = new Funcionario(0, "Ana Silva", " NOVO@OFICINA.COM ", null, PerfilFuncionario.ADMIN, false);
        
        when(repository.buscarPorId(1)).thenReturn(Optional.of(existente));
        when(repository.buscarPorEmail("novo@oficina.com")).thenReturn(Optional.empty());
        when(repository.salvar(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Funcionario atualizado = new AtualizarFuncionarioService(repository).atualizarFuncionario(1, novosDados);

        assertAll(
            () -> assertEquals("Ana Silva", atualizado.getNome()),
            () -> assertEquals("novo@oficina.com", atualizado.getEmail()),
            () -> assertEquals(PerfilFuncionario.ADMIN, atualizado.getPerfil()),
            () -> assertEquals("hash", atualizado.getSenhaHash()),
            () -> assertTrue(atualizado.isAtivo())
        );
    }

    @Test
    void deveLancarExcecaoAoAtualizarDadosInvalidos() {
        AtualizarFuncionarioService service = new AtualizarFuncionarioService(repository);

        assertThrows(IllegalArgumentException.class, () -> service.atualizarFuncionario(1, null));
        assertThrows(IllegalArgumentException.class, () -> service.atualizarFuncionario(1, new Funcionario(0, "", "e@e.com", null, PerfilFuncionario.MECANICO, true)));
    }

    @Test
    void deveLancarExcecaoAoAtualizarFuncionarioNaoEncontrado() {
        when(repository.buscarPorId(1)).thenReturn(Optional.empty());
        AtualizarFuncionarioService service = new AtualizarFuncionarioService(repository);
        Funcionario novosDados = new Funcionario(0, "Ana Silva", "novo@oficina.com", null, PerfilFuncionario.ADMIN, false);

        assertThrows(EntidadeNaoEncontradaException.class, () -> service.atualizarFuncionario(1, novosDados));
    }

    @Test
    void deveLancarExcecaoAoAtualizarEmailJaExistenteEmOutroFuncionario() {
        Funcionario existente = new Funcionario(1, "Ana", "ana@oficina.com", "hash", PerfilFuncionario.MECANICO, true);
        Funcionario outro = new Funcionario(2, "Joao", "joao@oficina.com", "hash2", PerfilFuncionario.MECANICO, true);
        Funcionario novosDados = new Funcionario(0, "Ana Silva", "joao@oficina.com", null, PerfilFuncionario.ADMIN, false);
        
        when(repository.buscarPorId(1)).thenReturn(Optional.of(existente));
        when(repository.buscarPorEmail("joao@oficina.com")).thenReturn(Optional.of(outro));

        AtualizarFuncionarioService service = new AtualizarFuncionarioService(repository);
        assertThrows(IllegalArgumentException.class, () -> service.atualizarFuncionario(1, novosDados));
    }

    @Test
    void deveAtivarFuncionarioComSucesso() {
        Funcionario inativo = new Funcionario(1, "Ana", "ana@oficina.com", "hash", PerfilFuncionario.MECANICO, false);
        when(repository.buscarPorId(1)).thenReturn(Optional.of(inativo));

        new AtivarFuncionarioService(repository).ativarFuncionario(1);

        verify(repository).salvar(argThat(Funcionario::isAtivo));
    }

    @Test
    void deveLancarExcecaoAoAtivarFuncionarioInexistente() {
        when(repository.buscarPorId(1)).thenReturn(Optional.empty());
        
        AtivarFuncionarioService service = new AtivarFuncionarioService(repository);
        assertThrows(EntidadeNaoEncontradaException.class, () -> service.ativarFuncionario(1));
    }

    @Test
    void deveInativarFuncionarioComSucesso() {
        Funcionario ativo = new Funcionario(1, "Ana", "ana@oficina.com", "hash", PerfilFuncionario.MECANICO, true);
        when(repository.buscarPorId(1)).thenReturn(Optional.of(ativo));

        new InativarFuncionarioService(repository).inativarFuncionario(1);

        verify(repository).salvar(argThat(f -> !f.isAtivo()));
    }

    @Test
    void deveLancarExcecaoAoInativarFuncionarioInexistente() {
        when(repository.buscarPorId(1)).thenReturn(Optional.empty());
        
        InativarFuncionarioService service = new InativarFuncionarioService(repository);
        assertThrows(EntidadeNaoEncontradaException.class, () -> service.inativarFuncionario(1));
    }
}
