package com.oficina.estoque.infrastructure.adapters.outbound.persistence.repository;

import com.oficina.estoque.infrastructure.adapters.outbound.persistence.entity.ServicoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServicoJpaRepository extends JpaRepository<ServicoEntity, Integer> {}
