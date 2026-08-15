package com.oficina.administrativo.infrastructure.adapters.inbound.rest;

import com.oficina.administrativo.domain.model.Funcionario;
import com.oficina.administrativo.domain.model.FuncionarioAutenticado;
import com.oficina.administrativo.domain.model.PerfilFuncionario;
import com.oficina.administrativo.domain.ports.inbound.AutenticarFuncionarioUseCase;
import com.oficina.administrativo.infrastructure.adapters.inbound.rest.dto.AutenticacaoRequestDTO;
import com.oficina.administrativo.infrastructure.adapters.inbound.rest.dto.FuncionarioAutenticadoResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AutenticacaoFuncionarioControllerTest {
    private final AutenticarFuncionarioUseCase autenticarFuncionario = mock(AutenticarFuncionarioUseCase.class);
    private final AutenticacaoFuncionarioController controller = new AutenticacaoFuncionarioController(autenticarFuncionario);

    @Test
    void deveAutenticarFuncionarioComSucesso() {
        Funcionario funcionario = new Funcionario(1, "Ana", "ana@oficina.com", "hash", PerfilFuncionario.ADMIN, true);
        FuncionarioAutenticado autenticado = new FuncionarioAutenticado(funcionario, "token-jwt");
        when(autenticarFuncionario.autenticar("ana@oficina.com", "senha123")).thenReturn(autenticado);

        AutenticacaoRequestDTO request = new AutenticacaoRequestDTO("ana@oficina.com", "senha123");
        ResponseEntity<FuncionarioAutenticadoResponseDTO> response = controller.autenticar(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().id());
        assertEquals("Ana", response.getBody().nome());
        assertEquals("ana@oficina.com", response.getBody().email());
        assertEquals("token-jwt", response.getBody().token());
        assertEquals(PerfilFuncionario.ADMIN, response.getBody().perfil());
    }

    @Test
    void deveLancarExcecaoQuandoCredenciaisForemInvalidas() {
        when(autenticarFuncionario.autenticar("invalido@oficina.com", "senhaErrada"))
                .thenThrow(new IllegalArgumentException("Credenciais inválidas"));

        AutenticacaoRequestDTO request = new AutenticacaoRequestDTO("invalido@oficina.com", "senhaErrada");

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> controller.autenticar(request));

        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());
        assertEquals("Credenciais inválidas", exception.getReason());
    }
}
