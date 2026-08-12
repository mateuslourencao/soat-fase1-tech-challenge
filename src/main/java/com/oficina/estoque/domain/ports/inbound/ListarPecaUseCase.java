package com.oficina.estoque.domain.ports.inbound;

import com.oficina.estoque.domain.model.Peca;

import java.util.List;

public interface ListarPecaUseCase {
    List<Peca> listarPecas();
}
