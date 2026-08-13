package com.oficina.estoque.infrastructure.adapters.outbound.persistence.repository;

import com.oficina.estoque.infrastructure.adapters.outbound.persistence.entity.PecaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PecaJpaRepository extends JpaRepository<PecaEntity, Integer> {}
