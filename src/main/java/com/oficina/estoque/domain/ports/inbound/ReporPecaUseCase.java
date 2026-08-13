package com.oficina.estoque.domain.ports.inbound;

import com.oficina.estoque.domain.model.Peca;

import java.util.UUID;

public interface ReporPecaUseCase {
    Peca reporEstoque(UUID pecaID,  int quantidadeRepor);
}
