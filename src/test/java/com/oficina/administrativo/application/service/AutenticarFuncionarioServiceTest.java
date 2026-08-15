package com.oficina.administrativo.application.service;

import com.oficina.administrativo.domain.model.Funcionario;
import com.oficina.administrativo.domain.model.FuncionarioAutenticado;
import com.oficina.administrativo.domain.model.PerfilFuncionario;
import com.oficina.administrativo.domain.ports.outbound.FuncionarioRepositoryPort;
import com.oficina.administrativo.domain.ports.outbound.SenhaCriptografadaPort;
import com.oficina.administrativo.domain.ports.outbound.TokenJwtPort;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AutenticarFuncionarioServiceTest {
    private final FuncionarioRepositoryPort repository = mock(FuncionarioRepositoryPort.class);
    private final SenhaCriptografadaPort senhaCriptografada = mock(SenhaCriptografadaPort.class);
    private final TokenJwtPort tokenJwt = mock(TokenJwtPort.class);
    private final AutenticarFuncionarioService service = new AutenticarFuncionarioService(repository, senhaCriptografada, tokenJwt);

    @Test void deveAutenticarFuncionarioComSucesso() {
        Funcionario funcionario = new Funcionario(1, "Admin", "admin@oficina.com", "hash", PerfilFuncionario.ADMIN, true);
        when(repository.buscarPorEmail("admin@oficina.com")).thenReturn(Optional.of(funcionario));
        when(senhaCriptografada.confere("senha123", "hash")).thenReturn(true);
        when(tokenJwt.gerar(funcionario)).thenReturn("jwt-token");

        FuncionarioAutenticado resultado = service.autenticar("admin@oficina.com", "senha123");

        assertNotNull(resultado);
        assertEquals("jwt-token", resultado.token());
        assertEquals(funcionario, resultado.funcionario());
    }

    @Test void deveLancarExcecaoQuandoEmailNaoEncontrado() {
        when(repository.buscarPorEmail("invalido@oficina.com")).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> service.autenticar("invalido@oficina.com", "senha123"));
        assertEquals("Credenciais invalidas", exception.getMessage());
    }

    @Test void deveLancarExcecaoQuandoSenhaIncorreta() {
        Funcionario funcionario = new Funcionario(1, "Admin", "admin@oficina.com", "hash", PerfilFuncionario.ADMIN, true);
        when(repository.buscarPorEmail("admin@oficina.com")).thenReturn(Optional.of(funcionario));
        when(senhaCriptografada.confere("senha_errada", "hash")).thenReturn(false);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> service.autenticar("admin@oficina.com", "senha_errada"));
        assertEquals("Credenciais invalidas", exception.getMessage());
    }

    @Test void deveLancarExcecaoQuandoFuncionarioInativo() {
        Funcionario funcionario = new Funcionario(1, "Admin", "admin@oficina.com", "hash", PerfilFuncionario.ADMIN, false);
        when(repository.buscarPorEmail("admin@oficina.com")).thenReturn(Optional.of(funcionario));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> service.autenticar("admin@oficina.com", "senha123"));
        assertEquals("Credenciais invalidas", exception.getMessage());
    }

    @Test void deveLancarExcecaoQuandoDadosEntradaInvalidos() {
        assertThrows(IllegalArgumentException.class, () -> service.autenticar(null, "senha"));
        assertThrows(IllegalArgumentException.class, () -> service.autenticar("", "senha"));
        assertThrows(IllegalArgumentException.class, () -> service.autenticar("email", null));
        assertThrows(IllegalArgumentException.class, () -> service.autenticar("email", ""));
    }
}
