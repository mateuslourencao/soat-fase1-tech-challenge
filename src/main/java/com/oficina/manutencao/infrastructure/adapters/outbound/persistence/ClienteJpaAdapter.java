package com.oficina.manutencao.infrastructure.adapters.outbound.persistence;

import com.oficina.manutencao.domain.model.Cliente;
import com.oficina.manutencao.domain.ports.outbound.ClienteRepositoryPort;
import com.oficina.manutencao.infrastructure.adapters.outbound.persistence.repository.ClienteJpaRepository;
import com.oficina.manutencao.infrastructure.adapters.outbound.persistence.mapper.ClientePersistenceMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class ClienteJpaAdapter implements ClienteRepositoryPort {
    private final ClienteJpaRepository repository;
    private final ClientePersistenceMapper mapper;

    public ClienteJpaAdapter(ClienteJpaRepository repository, ClientePersistenceMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Cliente salvar(Cliente cliente) {
        return mapper.toDomain(repository.save(mapper.toEntity(cliente)));
    }

    @Override
    public Optional<Cliente> buscarPorId(UUID id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Cliente> listarTodos() { return repository.findAll().stream().map(mapper::toDomain).collect(Collectors.toList()); }

    @Override
    public void remover(UUID id) {repository.deleteById(id);}

}
