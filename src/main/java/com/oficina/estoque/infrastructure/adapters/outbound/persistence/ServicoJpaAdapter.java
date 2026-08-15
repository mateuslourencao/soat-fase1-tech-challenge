package com.oficina.estoque.infrastructure.adapters.outbound.persistence;

import com.oficina.estoque.domain.model.Servico;
import com.oficina.estoque.domain.ports.outbound.ServicoRepositoryPort;
import com.oficina.estoque.infrastructure.adapters.outbound.persistence.mapper.ServicoPersistenceMapper;
import com.oficina.estoque.infrastructure.adapters.outbound.persistence.repository.ServicoJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ServicoJpaAdapter implements ServicoRepositoryPort {
    private final ServicoJpaRepository repository;
    private final ServicoPersistenceMapper mapper;

    public ServicoJpaAdapter(ServicoJpaRepository repository, ServicoPersistenceMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Servico salvar(String descricao, Double valor) {
        Servico servico = new Servico(descricao, valor);
        return mapper.toDomain(repository.save(mapper.toEntity(servico)));
    }

    @Override
    public Optional<Servico> buscarPorId(int id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Servico> listarServicos() {
        return repository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public Servico atualizarServico(Servico servico) {
        return mapper.toDomain(repository.save(mapper.toEntity(servico)));
    }

    @Override
    public Void removerServico(int id) {
        repository.deleteById(id);
        return null;
    }
}
