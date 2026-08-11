package com.oficina.estoque.domain.ports.inbound;

import com.oficina.estoque.domain.model.Peca;

public interface CadastrarPecaUseCase {
    Peca CadastrarPeca(String descricao, Double valor, int quantidade);
}
