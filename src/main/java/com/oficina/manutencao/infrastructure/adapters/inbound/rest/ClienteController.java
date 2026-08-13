package com.oficina.manutencao.infrastructure.adapters.inbound.rest;

import com.oficina.manutencao.domain.model.Cliente;
import com.oficina.manutencao.domain.ports.inbound.*;
import com.oficina.manutencao.infrastructure.adapters.inbound.rest.dto.ClienteRequestDTO;
import com.oficina.manutencao.infrastructure.adapters.inbound.rest.dto.ClienteResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
        Cliente cliente = new Cliente(request.documento(), request.nome(), request.email(), request.telefone());
        Cliente salvo = cadastrarCliente.cadastrarCliente(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(converterParaDTO(salvo));
    }

    @GetMapping
    public ResponseEntity<List<Cliente>> listar() {
        return ResponseEntity.ok(listarClientes.listarClientes());
    }

    @GetMapping("/{documento}")
    public ResponseEntity<ClienteResponseDTO> buscarPorId(@PathVariable String documento) {
        Cliente cliente = buscarCliente.buscarCliente(documento);
        return ResponseEntity.ok(converterParaDTO(cliente));
    }

    @PutMapping("/{documento}")
    public ResponseEntity<ClienteResponseDTO> atualizar(@PathVariable String documento, @RequestBody @Valid ClienteRequestDTO request) {
        Cliente clienteAtualizado = new Cliente(documento, request.nome(), request.email(), request.telefone());
        Cliente salvo = atualizarCliente.atualizarCliente(documento, clienteAtualizado);
        return ResponseEntity.ok(converterParaDTO(salvo));
    }

    @DeleteMapping("/{documento}")
    public ResponseEntity<Void> remover(@PathVariable String documento) {
        removerCliente.removerCliente(documento);
        return ResponseEntity.noContent().build();
    }

    private ClienteResponseDTO converterParaDTO(Cliente cliente) {
        return new ClienteResponseDTO(cliente.getDocumento(), cliente.getNome(), cliente.getEmail(), cliente.getTelefone());
    }
}