package com.oficina.administrativo.infrastructure.adapters.outbound.persistence.repository;

import com.oficina.administrativo.infrastructure.adapters.outbound.persistence.entity.FuncionarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FuncionarioJpaRepository extends JpaRepository<FuncionarioEntity, Integer> {
    Optional<FuncionarioEntity> findByEmail(String email);
}
