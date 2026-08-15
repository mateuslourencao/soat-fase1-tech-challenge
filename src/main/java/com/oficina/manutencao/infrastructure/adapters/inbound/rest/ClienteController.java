package com.oficina.manutencao.infrastructure.adapters.inbound.rest;

import com.oficina.common.domain.validation.CpfCnpj;
import com.oficina.manutencao.domain.model.Cliente;
import com.oficina.manutencao.domain.ports.inbound.*;
import com.oficina.manutencao.infrastructure.adapters.inbound.rest.dto.ClienteRequestDTO;
import com.oficina.manutencao.infrastructure.adapters.inbound.rest.dto.ClienteResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/clientes")
@Tag(name = "Clientes", description = "Gestão de clientes da oficina")
@SecurityRequirement(name = "bearerAuth")
@Validated
public class ClienteController {
    private static final String NON_ALPHANUMERIC_REGEX = "[^a-zA-Z0-9]";

    private final CadastrarClienteUseCase cadastrarCliente;
    private final AtualizarClienteUseCase atualizarCliente;
    private final BuscarClienteUseCase buscarCliente;
    private final ListarClientesUseCase listarClientes;
    private final RemoverClienteUseCase removerCliente;

    public ClienteController(CadastrarClienteUseCase cadastrarCliente, AtualizarClienteUseCase atualizarCliente,
                             BuscarClienteUseCase buscarCliente, ListarClientesUseCase listarClientes, RemoverClienteUseCase removerCliente) {
        this.cadastrarCliente = cadastrarCliente;
        this.atualizarCliente = atualizarCliente;
        this.buscarCliente = buscarCliente;
        this.listarClientes = listarClientes;
        this.removerCliente = removerCliente;
    }

    @PostMapping
    @Operation(summary = "Cadastrar cliente", description = "Cadastra um novo cliente no sistema")
    @ApiResponse(responseCode = "201", description = "Cliente cadastrado com sucesso")
    public ResponseEntity<ClienteResponseDTO> criar(@RequestBody @Valid ClienteRequestDTO request) {
        String documentoLimpo = request.documento().replaceAll(NON_ALPHANUMERIC_REGEX, "");
        Cliente cliente = new Cliente(documentoLimpo, request.nome(), request.email(), request.telefone());
        Cliente salvo = cadastrarCliente.cadastrarCliente(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(converterParaDTO(salvo));
    }

    @GetMapping
    @Operation(summary = "Listar clientes", description = "Retorna uma lista de todos os clientes cadastrados")
    @ApiResponse(responseCode = "200", description = "Lista de clientes retornada com sucesso")
    public ResponseEntity<List<ClienteResponseDTO>> listar() {
        List<ClienteResponseDTO> response = listarClientes.listarClientes().stream()
                .map(this::converterParaDTO)
                .toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{documento}")
    @Operation(summary = "Buscar cliente por documento", description = "Retorna os detalhes de um cliente através do seu CPF ou CNPJ")
    @ApiResponse(responseCode = "200", description = "Cliente encontrado")
    @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    public ResponseEntity<ClienteResponseDTO> buscarPorId(@Parameter(description = "Documento do cliente (CPF/CNPJ)") @PathVariable @CpfCnpj String documento) {
        String documentoLimpo = documento.replaceAll(NON_ALPHANUMERIC_REGEX, "");
        Cliente cliente = buscarCliente.buscarCliente(documentoLimpo);
        return ResponseEntity.ok(converterParaDTO(cliente));
    }

    @PutMapping("/{documento}")
    @Operation(summary = "Atualizar cliente", description = "Atualiza os dados de um cliente existente")
    @ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso")
    public ResponseEntity<ClienteResponseDTO> atualizar(@Parameter(description = "Documento do cliente (CPF/CNPJ)") @PathVariable @CpfCnpj String documento, @RequestBody @Valid ClienteRequestDTO request) {
        String documentoLimpo = documento.replaceAll(NON_ALPHANUMERIC_REGEX, "");
        Cliente clienteAtualizado = new Cliente(documentoLimpo, request.nome(), request.email(), request.telefone());
        Cliente salvo = atualizarCliente.atualizarCliente(documentoLimpo, clienteAtualizado);
        return ResponseEntity.ok(converterParaDTO(salvo));
    }

    @DeleteMapping("/{documento}")
    @Operation(summary = "Remover cliente", description = "Remove o cadastro de um cliente do sistema")
    @ApiResponse(responseCode = "204", description = "Cliente removido com sucesso")
    public ResponseEntity<Void> remover(@Parameter(description = "Documento do cliente (CPF/CNPJ)") @PathVariable @CpfCnpj String documento) {
        String documentoLimpo = documento.replaceAll(NON_ALPHANUMERIC_REGEX, "");
        removerCliente.removerCliente(documentoLimpo);
        return ResponseEntity.noContent().build();
    }

    private ClienteResponseDTO converterParaDTO(Cliente cliente) {
        return new ClienteResponseDTO(cliente.getDocumento(), cliente.getNome(), cliente.getEmail(), cliente.getTelefone());
    }
}