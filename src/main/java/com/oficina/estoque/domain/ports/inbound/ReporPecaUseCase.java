package com.oficina.estoque.domain.ports.inbound;

import com.oficina.estoque.domain.model.Peca;

public interface ReporPecaUseCase {
    Peca reporEstoque(int pecaID,  int quantidadeRepor);
}
