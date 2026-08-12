package com.oficina.estoque.infrastructure.adapters.outbound.persistence;

import com.oficina.estoque.domain.model.Servico;
import com.oficina.estoque.domain.ports.outbound.ServicoRepositoryPort;
import com.oficina.estoque.infrastructure.adapters.outbound.persistence.mapper.ServicoPersistenceMapper;
import com.oficina.estoque.infrastructure.adapters.outbound.persistence.repository.ServicoJpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class ServicoJpaAdapter implements ServicoRepositoryPort {
    private final ServicoJpaRepository repository;
    private final ServicoPersistenceMapper mapper;

    public ServicoJpaAdapter(ServicoJpaRepository repository, ServicoPersistenceMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Servico salvar(Servico servico) {
        return mapper.toDomain(repository.save(mapper.toEntity(servico)));
    }

    @Override
    public Optional<Servico> buscarPorId(UUID id) {
        return repository.findById(id).map(mapper::toDomain);
    }
}
