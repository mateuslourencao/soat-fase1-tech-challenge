package com.oficina.manutencao.infrastructure.adapters.inbound.rest;

import com.oficina.manutencao.application.service.*;
import com.oficina.manutencao.domain.model.Cliente;
import com.oficina.manutencao.domain.ports.inbound.*;
import com.oficina.manutencao.infrastructure.adapters.inbound.rest.dto.ClienteRequestDTO;
import com.oficina.manutencao.infrastructure.adapters.inbound.rest.dto.ClienteResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/clientes")
public class ClienteController {

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
    public ResponseEntity<ClienteResponseDTO> criar(@RequestBody @Valid ClienteRequestDTO request) {
        Cliente cliente = new Cliente(UUID.randomUUID(), request.nome(), request.email(), request.documento(), request.telefone());
        Cliente salvo = cadastrarCliente.cadastrarCliente(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(converterParaDTO(salvo));
    }

    @GetMapping
    public ResponseEntity<List<ClienteResponseDTO>> listar() {
        List<ClienteResponseDTO> clientes = listarClientes.listarClientes().stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> buscarPorId(@PathVariable UUID id) {
        Cliente cliente = buscarCliente.buscarCliente(id);
        return ResponseEntity.ok(converterParaDTO(cliente));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> atualizar(@PathVariable UUID id, @RequestBody @Valid ClienteRequestDTO request) {
        Cliente clienteAtualizado = new Cliente(id, request.nome(), request.email(), request.documento(), request.telefone());
        Cliente salvo = atualizarCliente.atualizarCliente(id, clienteAtualizado);
        return ResponseEntity.ok(converterParaDTO(salvo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable UUID id) {
        removerCliente.removerCliente(id);
        return ResponseEntity.noContent().build();
    }

    // Método auxiliar interno para conversão
    private ClienteResponseDTO converterParaDTO(Cliente cliente) {
        return new ClienteResponseDTO(cliente.getId(), cliente.getNome(), cliente.getEmail(), cliente.getDocumento(), cliente.getTelefone());
    }
}