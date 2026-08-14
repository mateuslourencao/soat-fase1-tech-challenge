package com.oficina.administrativo.application.service;

import com.oficina.administrativo.domain.model.Funcionario;
import com.oficina.administrativo.domain.model.FuncionarioAutenticado;
import com.oficina.administrativo.domain.model.PerfilFuncionario;
import com.oficina.administrativo.domain.ports.outbound.FuncionarioRepositoryPort;
import com.oficina.administrativo.domain.ports.outbound.SenhaCriptografadaPort;
import com.oficina.administrativo.domain.ports.outbound.TokenJwtPort;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

class AutenticarFuncionarioServiceTest {
    @Test
    void deveAutenticarFuncionarioAtivoComCredenciaisValidas() {
        FuncionarioRepositoryPort repository = mock(FuncionarioRepositoryPort.class);
        SenhaCriptografadaPort senha = mock(SenhaCriptografadaPort.class);
        TokenJwtPort token = mock(TokenJwtPort.class);
        Funcionario funcionario = new Funcionario(1, "Ana", "ana@oficina.com", "hash", PerfilFuncionario.ADMIN, true);
        when(repository.buscarPorEmail("ana@oficina.com")).thenReturn(Optional.of(funcionario));
        when(senha.confere("segredo", "hash")).thenReturn(true);
        when(token.gerar(funcionario)).thenReturn("jwt");

        FuncionarioAutenticado autenticado = new AutenticarFuncionarioService(repository, senha, token)
                .autenticar(" ANA@OFICINA.COM ", "segredo");

        assertSame(funcionario, autenticado.funcionario());
        assertEquals("jwt", autenticado.token());
    }
}
