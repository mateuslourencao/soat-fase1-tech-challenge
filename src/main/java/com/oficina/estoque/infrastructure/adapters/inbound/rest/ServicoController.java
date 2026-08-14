package com.oficina.estoque.infrastructure.adapters.inbound.rest;

import com.oficina.estoque.domain.model.Servico;
import com.oficina.estoque.domain.ports.inbound.*;
import com.oficina.estoque.infrastructure.adapters.inbound.rest.dto.ServicoRequestDTO;
import com.oficina.estoque.infrastructure.adapters.inbound.rest.dto.ServicoResponseDTO;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/v1/servicos")
public class ServicoController {

    ListarServicoUseCase listarServicos;
    CadastrarServicoUseCase cadastrarServico;
    AtualizarServicoUseCase atualizarServico;
    RemoverServicoUseCase removerServico;
    BuscarServicoUseCase buscarServico;

    ServicoController(ListarServicoUseCase listarServicos, CadastrarServicoUseCase cadastrarServico,
                      AtualizarServicoUseCase atualizarServico, RemoverServicoUseCase removerServico, BuscarServicoUseCase buscarServico){
        this.listarServicos = listarServicos;
        this.cadastrarServico = cadastrarServico;
        this.atualizarServico = atualizarServico;
        this.removerServico = removerServico;
        this.buscarServico = buscarServico;
    }

    @GetMapping
    public ResponseEntity<List<Servico>> listar() {
        return ResponseEntity.ok(listarServicos.listarServico());
    }

    @PostMapping
    public ResponseEntity<ServicoResponseDTO> criar(@Valid @RequestBody ServicoRequestDTO request) {
        Servico novoServico = cadastrarServico.cadastrarServico(request.descricao(), request.valor());
        ServicoResponseDTO response = new ServicoResponseDTO(novoServico);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServicoResponseDTO> buscarPorId(@PathVariable int id) {
        Servico servico = buscarServico.buscarServico(id);
        return ResponseEntity.ok(new ServicoResponseDTO(servico.getId(), servico.getDescricao(), servico.getValor()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable int id) {
        removerServico.removerServico(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServicoResponseDTO>  atualizar(@PathVariable int id, @Valid @RequestBody ServicoRequestDTO request) {
        Servico servico = new Servico(id, request.descricao(), request.valor());
        atualizarServico.atualizarServico(servico);
        return ResponseEntity.ok(new ServicoResponseDTO(servico));
    }

}
