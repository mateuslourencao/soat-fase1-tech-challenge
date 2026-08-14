package com.oficina.estoque.domain.ports.inbound;

import com.oficina.estoque.domain.model.Peca;

public interface AtualizarPecaUseCase {
    Peca atualizarPeca(Peca peca);
}
