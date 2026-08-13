package com.oficina.manutencao.infrastructure.adapters.outbound.persistence.repository;

import com.oficina.manutencao.infrastructure.adapters.outbound.persistence.entity.VeiculoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VeiculoJpaRepository extends JpaRepository<VeiculoEntity, String> {}
