package com.oficina.estoque.infrastructure.adapters.outbound.persistence.repository;

import com.oficina.estoque.infrastructure.adapters.outbound.persistence.entity.ServicoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ServicoJpaRepository extends JpaRepository<ServicoEntity, UUID> {}
