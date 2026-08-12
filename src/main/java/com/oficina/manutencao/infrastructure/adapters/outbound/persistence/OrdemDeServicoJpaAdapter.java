package com.oficina.manutencao.infrastructure.adapters.outbound.persistence;

import com.oficina.manutencao.domain.model.OrdemDeServico;
import com.oficina.manutencao.domain.ports.outbound.OrdemDeServicoRepositoryPort;
import com.oficina.manutencao.infrastructure.adapters.outbound.persistence.entity.OrdemDeServicoEntity;
import com.oficina.manutencao.infrastructure.adapters.outbound.persistence.mapper.OrdemDeServicoPersistenceMapper;
import com.oficina.manutencao.infrastructure.adapters.outbound.persistence.repository.OrdemDeServicoJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class OrdemDeServicoJpaAdapter
        implements OrdemDeServicoRepositoryPort {

    private final OrdemDeServicoJpaRepository repository;
    private final OrdemDeServicoPersistenceMapper mapper;

    public OrdemDeServicoJpaAdapter(
            OrdemDeServicoJpaRepository repository,
            OrdemDeServicoPersistenceMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public OrdemDeServico salvar(OrdemDeServico ordem) {
        OrdemDeServicoEntity entity = mapper.toEntity(ordem);
        OrdemDeServicoEntity salva = repository.save(entity);
        return mapper.toDomain(salva);
    }

    @Override
    public Optional<OrdemDeServico> buscarPorId(UUID id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<OrdemDeServico> listarOrdensDeServico() {
        List<OrdemDeServico> ordemDeServicos = repository.findAll().stream().map(mapper::toDomain).toList();
        return ordemDeServicos;
    }
}
