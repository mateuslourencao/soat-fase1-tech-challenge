package com.oficina.administrativo.infrastructure.adapters.inbound.rest;

import com.oficina.administrativo.domain.model.Funcionario;
import com.oficina.administrativo.domain.model.PerfilFuncionario;
import com.oficina.administrativo.domain.ports.inbound.*;
import com.oficina.administrativo.infrastructure.adapters.inbound.rest.dto.AtualizarFuncionarioRequestDTO;
import com.oficina.administrativo.infrastructure.adapters.inbound.rest.dto.FuncionarioRequestDTO;
import com.oficina.administrativo.infrastructure.adapters.inbound.rest.dto.FuncionarioResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class FuncionarioControllerTest {
    private final CadastrarFuncionarioUseCase cadastrar = mock(CadastrarFuncionarioUseCase.class);
    private final BuscarFuncionarioUseCase buscar = mock(BuscarFuncionarioUseCase.class);
    private final ListarFuncionariosUseCase listar = mock(ListarFuncionariosUseCase.class);
    private final AtualizarFuncionarioUseCase atualizar = mock(AtualizarFuncionarioUseCase.class);
    private final AtivarFuncionarioUseCase ativar = mock(AtivarFuncionarioUseCase.class);
    private final InativarFuncionarioUseCase inativar = mock(InativarFuncionarioUseCase.class);
    private final FuncionarioController controller = new FuncionarioController(cadastrar, buscar, listar, atualizar, ativar, inativar);

    @Test
    void deveCadastrarBuscarListarEAtualizarFuncionario() {
        Funcionario funcionario = new Funcionario(1, "Ana", "ana@oficina.com", "hash", PerfilFuncionario.ADMIN, true);
        when(cadastrar.cadastrarFuncionario(any(), eq("segredo"))).thenReturn(funcionario);
        when(buscar.buscarFuncionario(1)).thenReturn(funcionario);
        when(listar.listarFuncionarios()).thenReturn(List.of(funcionario));
        when(atualizar.atualizarFuncionario(eq(1), any())).thenReturn(funcionario);

        ResponseEntity<FuncionarioResponseDTO> criado = controller.cadastrar(new FuncionarioRequestDTO("Ana", "ana@oficina.com", "segredo", PerfilFuncionario.ADMIN));
        assertAll(() -> assertEquals(HttpStatus.CREATED, criado.getStatusCode()), () -> assertEquals(1, criado.getBody().id()));
        assertEquals("Ana", controller.buscarPorId(1).getBody().nome());
        assertEquals(1, controller.listar().getBody().size());
        assertEquals("ana@oficina.com", controller.atualizar(1,
                new AtualizarFuncionarioRequestDTO("Ana", "ana@oficina.com", PerfilFuncionario.ADMIN)).getBody().email());
    }

    @Test
    void deveDelegarAlteracoesDeStatus() {
        controller.ativar(1);
        controller.inativar(1);

        verify(ativar).ativarFuncionario(1);
        verify(inativar).inativarFuncionario(1);
    }
}
